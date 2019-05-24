package com.hing.stackoverflowuser.presenter.userreputation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
    lateinit var userReputationViewModel: UserReputationViewModel
    @Inject
    lateinit var networkHelper: NetworkHelper
    @Inject
    lateinit var dateTimeHelper: DateTimeHelper

    private lateinit var userReputationAdapter: UserReputationAdapter
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_user_reputation)

        userId = intent?.extras?.getInt(EXTRA_USER_ID) ?: 0
        initData()
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        if (!networkHelper.isConnectedToInternet()) {
            userReputationList.showSnackbar(getString(R.string.no_internet_connection) ?: "")
        }
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

    companion object {
        private const val VISIBLE_THRESHOLD = 10
        private const val PER_PAGE_ITEM = 30
        private const val SITE = "stackoverflow"

        const val EXTRA_USER_ID = "USER_ID"
    }
}
