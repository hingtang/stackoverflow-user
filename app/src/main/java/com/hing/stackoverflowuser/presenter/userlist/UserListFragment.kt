package com.hing.stackoverflowuser.presenter.userlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hing.stackoverflowuser.R
import com.hing.stackoverflowuser.listeners.OnLoadMoreListener
import com.hing.stackoverflowuser.navigators.UserItemNavigator
import com.hing.stackoverflowuser.presenter.userreputation.UserReputationActivity
import com.hing.stackoverflowuser.presenter.userreputation.UserReputationActivity.Companion.EXTRA_USER_ID
import com.hing.stackoverflowuser.utils.DateTimeHelper
import com.hing.stackoverflowuser.utils.NetworkHelper
import com.hing.stackoverflowuser.utils.showSnackbar
import com.hing.stackoverflowuser.utils.showToast
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_user_list.progress_bar as progressBar
import kotlinx.android.synthetic.main.fragment_user_list.user_list as userListRecyclerView

/**
 * Created by HingTang on 2019-05-23.
 */
class UserListFragment : Fragment(), UserItemNavigator {

    @Inject
    lateinit var userListViewModel: UserListViewModel
    @Inject
    lateinit var networkHelper: NetworkHelper
    @Inject
    lateinit var dateTimeHelper: DateTimeHelper

    private lateinit var userListAdapter: UserListAdapter
    private var firstVisibleItemPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            SAVE_POSITION,
            (userListRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        firstVisibleItemPosition = savedInstanceState?.getInt(SAVE_POSITION, 0) ?: 0
    }

    override fun onStart() {
        super.onStart()
        if (!networkHelper.isConnectedToInternet()) {
            userListRecyclerView.showSnackbar(context?.getString(R.string.no_internet_connection) ?: "")
        }
    }

    override fun openUserDetail(userId: Int) {
        activity?.startActivity(Intent(activity, UserReputationActivity::class.java).apply {
            putExtra(EXTRA_USER_ID, userId)
        })
    }

    private fun initData() {
        with(userListViewModel) {
            userList.observe(this@UserListFragment, Observer {
                userListAdapter.updateUserList(it)
            })

            errorMessage.observe(this@UserListFragment, Observer {
                userListRecyclerView.showToast(it)
            })

            isLoading.observe(this@UserListFragment, Observer { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            })

            loadUserList(1, PER_PAGE_ITEM, SITE)
        }
    }

    private fun initRecyclerView() {
        userListAdapter = UserListAdapter({ userId: Int -> openUserDetail(userId) }, dateTimeHelper)
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
                    userListViewModel.loadUserList(currentPage, PER_PAGE_ITEM, SITE)
                }
            })
            userListViewModel.userList.value?.let {
                if (firstVisibleItemPosition < it.size) {
                    scrollToPosition(firstVisibleItemPosition)
                }
            }
            addItemDecoration(dividerItemDecoration)
        }
    }

    private companion object {
        const val VISIBLE_THRESHOLD = 10
        const val PER_PAGE_ITEM = 30
        const val SITE = "stackoverflow"
        const val SAVE_POSITION = "SAVE_POSITION"
    }
}
