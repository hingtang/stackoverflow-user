package com.hing.stackoverflowuser.presenter.userlist

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.hing.githubuserlist.customaction.recyclerViewItemCount
import com.hing.githubuserlist.customaction.recyclerViewWithId
import com.hing.stackoverflowuser.R
import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.rules.InjectedFragmentTestRule
import com.hing.stackoverflowuser.utils.DateTimeHelper
import com.hing.stackoverflowuser.utils.NetworkHelper
import com.hing.stackoverflowuser.utils.waitUntil
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import java.util.concurrent.Callable

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

        waitUntil("finished update no internet status", Callable {
            screen.verifyShowNoInternetConnectionMessage()
            screen.verifySpinnerBookmarkSelected()
            verify(userListViewModel).loadBookmarkList()
            true
        }, 20000)
    }

    @Test
    fun should_not_show_message_no_internet_connection_when_it_has_internet_connected() {
        whenever(networkHelper.isConnectedToInternet()).thenReturn(true)

        screen.start()

        waitUntil("finished update internet status", Callable {
            screen.verifyNotShowNoInternetConnectionMessage()
            screen.verifySpinnerAllSelected()
            true
        }, 20000)
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
        errorMessage.postValue(ERROR_MESSAGE)

        screen.start()
        screen.verifyShowErrorMessage(ERROR_MESSAGE)
    }

    @Test
    fun should_trigger_load_more_when_user_scroll() {
        userList.postValue(userListData)
        screen.start()
        screen.scrollListToEnd()

        //load 2: 1 for first start, second for load more
        verify(userListViewModel, times(2)).loadUserList(
            anyInt(), anyInt(), anyString(), anyBoolean()
        )
    }

    @Test
    fun should_trigger_load_bookmarked_user_when_bookmarked_spinner_selected() {
        screen.start()
        screen.spinnerBookmarkedSelected()

        verify(userListViewModel).loadBookmarkList()
    }

    @Test
    fun should_trigger_load_user_list_when_all_spinner_selected() {
        screen.start()
        screen.spinnerAllSelected()

        //load 2: 1 for first start, second for select spinner
        verify(userListViewModel, times(2)).loadUserList(eq(1), anyInt(), anyString(), eq(false))
    }

    inner class UserListScreen {
        fun start() {
            rule.launchFragment(UserListFragment())
        }

        fun verifyShowNoInternetConnectionMessage() {
            onView(withText(R.string.no_internet_connection)).check(matches(isDisplayed()))
        }

        fun verifyNotShowNoInternetConnectionMessage() {
            onView(withText(R.string.no_internet_connection)).check(doesNotExist())
        }

        fun verifyShowProgressBar() {
            onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        }

        fun verifyHideProgressBar() {
            onView(withId(R.id.progress_bar)).check(matches(CoreMatchers.not(isDisplayed())))
        }

        fun verifyShowCorrectUserListDataSize() {
            onView(recyclerViewWithId(R.id.user_list))
                .check(recyclerViewItemCount(userListData.size))
        }

        fun verifyShowErrorMessage(error: String) {
            onView(withText(error))
                .inRoot(RootMatchers.withDecorView(CoreMatchers.not(rule.activity.window.decorView)))
                .check(matches(isDisplayed()))
        }

        fun verifySpinnerBookmarkSelected() {
            onView(withId(R.id.spinner_menu)).check(matches(withSpinnerText(containsString(BOOKMARKED_SPINNER_TEXT))))
        }

        fun verifySpinnerAllSelected() {
            onView(withId(R.id.spinner_menu)).check(matches(withSpinnerText(containsString(ALL_SPINNER_TEXT))))
        }

        fun scrollListToEnd() {
            onView(withId(R.id.user_list)).perform(
                RecyclerViewActions.scrollToPosition<UserListAdapter.UserViewHolder>(
                    userListData.size - 3
                )
            )
        }

        fun spinnerBookmarkedSelected() {
            onView(withId(R.id.spinner_menu)).perform(click())
            onData(allOf(`is`(instanceOf(String::class.java)), `is`(BOOKMARKED_SPINNER_TEXT))).perform(click())
        }

        fun spinnerAllSelected() {
            onView(withId(R.id.spinner_menu)).perform(click())
            onData(allOf(`is`(instanceOf(String::class.java)), `is`(ALL_SPINNER_TEXT))).perform(click())
        }
    }

    private fun mockData() {
        for (i in 0 until 10) {
            userListData.add(i, User(i + 1, lastAccessDate = 0, reputation = 0))
        }
    }

    private companion object {
        val userListData: MutableList<User> = mutableListOf()

        const val ERROR_MESSAGE = "ERROR_MESSAGE"
        const val BOOKMARKED_SPINNER_TEXT = "Bookmarked"
        const val ALL_SPINNER_TEXT = "All"
    }
}
