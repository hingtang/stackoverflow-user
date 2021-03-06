package com.hing.stackoverflowuser.ui.userlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hing.stackoverflowuser.R
import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.listeners.OnLoadMoreListener
import com.hing.stackoverflowuser.navigators.UserItemNavigator
import com.hing.stackoverflowuser.ui.userreputation.UserReputationActivity
import com.hing.stackoverflowuser.ui.userreputation.UserReputationActivity.Companion.EXTRA_USER_ID
import com.hing.stackoverflowuser.utils.DateTimeHelper
import com.hing.stackoverflowuser.utils.NetworkHelper
import com.hing.stackoverflowuser.utils.showSnackbar
import com.hing.stackoverflowuser.utils.showToast
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_user_list.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_user_list.progress_bar as progressBar
import kotlinx.android.synthetic.main.fragment_user_list.user_list as userListRecyclerView
import kotlinx.android.synthetic.main.toolbar_user_list.spinner_menu as spinnerMenu
import kotlinx.android.synthetic.main.toolbar_user_list.tv_title as toolbarTitle

/**
 * Created by HingTang on 2019-05-23.
 */
class UserListFragment : Fragment(), UserItemNavigator, AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var networkHelper: NetworkHelper
    @Inject
    lateinit var dateTimeHelper: DateTimeHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userListViewModel: UserListViewModel
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userListViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserListViewModel::class.java)
        setupToolbar()
        initSpinnerMenu()
        initRecyclerView()
        initData()
    }

    override fun onStart() {
        super.onStart()
        if (!networkHelper.isConnectedToInternet()) {
            userListRecyclerView.showSnackbar(context?.getString(R.string.no_internet_connection) ?: "")
            spinnerMenu.setSelection(1)
        }
    }

    override fun openUserDetail(userId: Int) {
        activity?.startActivity(Intent(activity, UserReputationActivity::class.java).apply {
            putExtra(EXTRA_USER_ID, userId)
        })
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //Ignore
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> userListViewModel.loadUserList(1, PER_PAGE_ITEM, SITE, false)
            1 -> userListViewModel.loadBookmarkList()
        }
    }

    private fun initData() {
        with(userListViewModel) {
            userList.observe(this@UserListFragment, Observer {
                if (getCurrentPage() > 1) {
                    userListAdapter.insertUserList(it)
                } else {
                    userListAdapter.updateUserList(it)
                }
            })

            errorMessage.observe(this@UserListFragment, Observer {
                userListRecyclerView.showToast(it)
            })

            isLoading.observe(this@UserListFragment, Observer { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            })

            loadUserList(1, PER_PAGE_ITEM, SITE, spinnerMenu.selectedItemPosition == 1)
        }
    }

    private fun initRecyclerView() {
        userListAdapter = UserListAdapter(
            { userId: Int -> openUserDetail(userId) },
            { user: User -> bookmarkUser(user) },
            dateTimeHelper
        )
        val linearLayoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this.context, linearLayoutManager.orientation)

        with(userListRecyclerView) {
            adapter = userListAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            addOnScrollListener(object : OnLoadMoreListener(VISIBLE_THRESHOLD, linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    val currentPage = userListViewModel.userList.value?.size ?: 0 / PER_PAGE_ITEM
                    userListViewModel.loadUserList(
                        currentPage,
                        PER_PAGE_ITEM,
                        SITE,
                        spinnerMenu.selectedItemPosition == 1
                    )
                }
            })
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun initSpinnerMenu() {
        spinnerMenu.onItemSelectedListener = this
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        toolbarTitle.text = getString(R.string.app_name)
    }

    private fun bookmarkUser(user: User) {
        userListViewModel.bookmarkUser(user)
    }

    private companion object {
        const val VISIBLE_THRESHOLD = 10
        const val PER_PAGE_ITEM = 30
        const val SITE = "stackoverflow"
    }
}
