package com.hing.stackoverflowuser.ui.userreputation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hing.stackoverflowuser.R
import com.hing.stackoverflowuser.listeners.OnLoadMoreListener
import com.hing.stackoverflowuser.utils.*
import dagger.android.AndroidInjection
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_user_reputation.progress_bar as progressBar
import kotlinx.android.synthetic.main.activity_user_reputation.tv_no_data as noDataText
import kotlinx.android.synthetic.main.activity_user_reputation.user_reputation_list as userReputationList

class UserReputationActivity : AppCompatActivity() {

    @Inject
    lateinit var networkHelper: NetworkHelper
    @Inject
    lateinit var dateTimeHelper: DateTimeHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userReputationAdapter: UserReputationAdapter
    private lateinit var userReputationViewModel: UserReputationViewModel
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        userReputationViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserReputationViewModel::class.java)
        setContentView(R.layout.activity_user_reputation)

        userId = intent?.extras?.getInt(EXTRA_USER_ID) ?: 0
        setupToolbar()
        initData()
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        if (!networkHelper.isConnectedToInternet()) {
            userReputationList.showSnackbar(getString(R.string.no_internet_connection) ?: "")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == android.R.id.home) {
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData() {
        with(userReputationViewModel) {
            userReputation.observe(this@UserReputationActivity, Observer {
                userReputationAdapter.updateUserReputation(it)
                noDataText.setVisible(it.isEmpty())
            })

            errorMessage.observe(this@UserReputationActivity, Observer {
                userReputationList.showToast(it)
            })

            isLoading.observe(this@UserReputationActivity, Observer { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            })

            getUserReputation(userId, 1, PER_PAGE_ITEM, SITE)
        }
    }

    private fun initRecyclerView() {
        userReputationAdapter = UserReputationAdapter(dateTimeHelper)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)

        with(userReputationList) {
            adapter = userReputationAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            addOnScrollListener(object : OnLoadMoreListener(VISIBLE_THRESHOLD, linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    val currentPage = userReputationViewModel.userReputation.value?.size ?: 0 / PER_PAGE_ITEM
                    userReputationViewModel.getUserReputation(userId, currentPage, PER_PAGE_ITEM, SITE)
                }
            })
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun setupToolbar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = getString(R.string.user_reputation_title)
        }
    }

    companion object {
        private const val VISIBLE_THRESHOLD = 10
        private const val PER_PAGE_ITEM = 30
        private const val SITE = "stackoverflow"

        const val EXTRA_USER_ID = "USER_ID"
    }
}
