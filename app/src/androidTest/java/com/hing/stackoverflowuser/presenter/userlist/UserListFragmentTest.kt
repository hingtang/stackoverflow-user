package com.hing.stackoverflowuser.presenter.userlist

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.hing.githubuserlist.customaction.recyclerViewItemCount
import com.hing.githubuserlist.customaction.recyclerViewWithId
import com.hing.stackoverflowuser.R
import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.rules.InjectedFragmentTestRule
import com.hing.stackoverflowuser.utils.DateTimeHelper
import com.hing.stackoverflowuser.utils.NetworkHelper
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

/**
 * Created by HingTang on 2019-05-23.
 */
class UserListFragmentTest {

    private lateinit var userListViewModel: UserListViewModel
    private lateinit var networkHelper: NetworkHelper
    private lateinit var dateTimeHelper: DateTimeHelper

    private val screen = UserListScreen()
    private val userList = MutableLiveData<List<User>>()
    private val errorMessage = MutableLiveData<String>()
    private val isLoadingData = MutableLiveData<Boolean>()

    @get:Rule
    val rule = InjectedFragmentTestRule<UserListFragment> {
        it.userListViewModel = userListViewModel
        it.networkHelper = networkHelper
        it.dateTimeHelper = dateTimeHelper
    }

    @Before
    fun setUp() {
        userListViewModel = mock {
            on { userList }.thenReturn(userList)
            on { errorMessage }.thenReturn(errorMessage)
            on { isLoading }.thenReturn(isLoadingData)
        }
        networkHelper = mock()
        dateTimeHelper = mock()

        mockData()
    }

    @Test
    fun should_show_message_no_internet_connection_when_no_internet_connected() {
        whenever(networkHelper.isConnectedToInternet()).thenReturn(false)

        screen.start()

        screen.verifyShowNoInternetConnectionMessage()
    }

    @Test
    fun should_not_show_message_no_internet_connection_when_it_has_internet_connected() {
        whenever(networkHelper.isConnectedToInternet()).thenReturn(true)

        screen.start()

        screen.verifyNotShowNoInternetConnectionMessage()
    }

    @Test
    fun should_show_progress_bar_when_loading_data() {
        isLoadingData.postValue(true)
        screen.start()

        screen.verifyShowProgressBar()
    }

    @Test
    fun should_hide_progress_bar_when_load_user_list_success() {
        isLoadingData.postValue(false)
        userList.postValue(userListData)
        screen.start()

        screen.verifyHideProgressBar()
        screen.verifyShowCorrectUserListDataSize()
    }

    @Test
    fun should_show_error_message_when_it_got_error_message() {
        errorMessage.postValue(error)

        screen.start()
        screen.verifyShowErrorMessage(error)
    }

    @Test
    fun should_trigger_load_more_when_user_scroll() {
        userList.postValue(userListData)
        screen.start()
        screen.scrollListToEnd()

        //load 2: 1 for first start, second for load more
        verify(userListViewModel, times(2)).loadUserList(
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.anyString()
        )
    }

    inner class UserListScreen {
        fun start() {
            rule.launchFragment(UserListFragment())
        }

        fun verifyShowNoInternetConnectionMessage() {
            Espresso.onView(ViewMatchers.withText(R.string.no_internet_connection))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun verifyNotShowNoInternetConnectionMessage() {
            Espresso.onView(ViewMatchers.withText(R.string.no_internet_connection)).check(ViewAssertions.doesNotExist())
        }

        fun verifyShowProgressBar() {
            Espresso.onView(ViewMatchers.withId(R.id.progress_bar))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun verifyHideProgressBar() {
            Espresso.onView(ViewMatchers.withId(R.id.progress_bar))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }

        fun verifyShowCorrectUserListDataSize() {
            Espresso.onView(recyclerViewWithId(R.id.user_list))
                .check(recyclerViewItemCount(userListData.size))
        }

        fun verifyShowErrorMessage(error: String) {
            Espresso.onView(ViewMatchers.withText(error))
                .inRoot(RootMatchers.withDecorView(CoreMatchers.not(rule.activity.window.decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun scrollListToEnd() {
            Espresso.onView(ViewMatchers.withId(R.id.user_list)).perform(
                RecyclerViewActions.scrollToPosition<UserListAdapter.UserViewHolder>(
                    userListData.size - 3
                )
            )
        }
    }

    private fun mockData() {
        for (i in 0 until 10) {
            userListData.add(i, User(i + 1, lastAccessDate = 0, reputation = 0))
        }
    }

    private companion object {
        val userListData: MutableList<User> = mutableListOf()

        const val error = "ERROR_MESSAGE"
    }
}
