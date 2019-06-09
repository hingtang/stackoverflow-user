package com.hing.stackoverflowuser.ui.userreputation

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import com.hing.stackoverflowuser.R
import com.hing.stackoverflowuser.data.UserReputation
import com.hing.stackoverflowuser.rules.InjectedActivityTestRule
import com.hing.stackoverflowuser.utils.DateTimeHelper
import com.hing.stackoverflowuser.utils.NetworkHelper
import com.hing.stackoverflowuser.utils.ViewModelUtil
import com.hing.stackoverflowuser.utils.waitUntil
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Callable

/**
 * Created by HingTang on 2019-05-24.
 */
class UserReputationActivityTest {

    private val user = MutableLiveData<List<UserReputation>>()
    private val errorMessage = MutableLiveData<String>()
    private val isLoadingData = MutableLiveData<Boolean>()

    private val viewModel: UserReputationViewModel = mock {
        on { userReputation }.thenReturn(user)
        on { errorMessage }.thenReturn(errorMessage)
        on { isLoading }.thenReturn(isLoadingData)
    }
    private val networkHelper: NetworkHelper = mock()
    private val dateTimeHelper: DateTimeHelper = mock()

    @get:Rule
    val rule = InjectedActivityTestRule(UserReputationActivity::class.java) {
        it.viewModelFactory = ViewModelUtil.createFor(viewModel)
        it.networkHelper = networkHelper
        it.dateTimeHelper = dateTimeHelper
    }

    @Before
    fun setUp() {
        user.postValue(userReputationList)
    }

    @Test
    fun should_show_message_no_internet_connection_when_no_internet_connected() {
        whenever(networkHelper.isConnectedToInternet()).thenReturn(false)

        waitUntil("finished update no internet status", Callable {
            onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(allOf(withText(R.string.no_internet_connection), isDisplayed())))
            true
        }, 20000)
    }

    @Test
    fun should_not_show_message_no_internet_connection_when_it_has_internet_connected() {
        whenever(networkHelper.isConnectedToInternet()).thenReturn(true)

        waitUntil("finished update internet status", Callable {
            onView(withId(com.google.android.material.R.id.snackbar_text)).check(doesNotExist())
            true
        }, 20000)
    }

    @Test
    fun should_show_progress_bar_when_loading_data() {
        isLoadingData.postValue(true)

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun should_hide_progress_bar_and_show_user_detail_when_fetch_user_detail_success() {
        isLoadingData.postValue(false)
        user.postValue(userReputationList)

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_show_error_message_when_it_got_error_message() {
        errorMessage.postValue(error)

        onView(withText(error)).inRoot(withDecorView(not(rule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun should_show_no_data_message_when_user_reputation_empty() {
        user.postValue(emptyList())

        onView(withId(R.id.tv_no_data)).check(matches(isDisplayed()))
    }

    @Test
    fun should_hide_not_data_message_when_user_reputation_is_not_empty() {
        user.postValue(userReputationList)

        onView(withId(R.id.tv_no_data)).check(matches(not(isDisplayed())))
    }

    private companion object {
        const val error = "ERROR_MESSAGE"

        val userReputationList = List(2) {
            UserReputation("type 1", 1212, 1212, 1)
            UserReputation("type 2", 2323, 2323, 2)
        }
    }
}
