package com.hing.stackoverflowuser.ui.userreputation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hing.stackoverflowuser.data.UserReputation
import com.hing.stackoverflowuser.data.UserReputationItems
import com.hing.stackoverflowuser.domain.GetUserReputationUseCase
import com.jraska.livedata.test
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by HingTang on 2019-05-24.
 */
class UserReputationViewModelTest {

    private lateinit var getUserReputationUseCase: GetUserReputationUseCase
    private lateinit var viewModel: UserReputationViewModel

    @get:Rule
    val instantExecutorRole = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        getUserReputationUseCase = mock()
        viewModel = UserReputationViewModel(
            getUserReputationUseCase,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun `should get the correct value when get user reputation list success`() {
        whenever(getUserReputationUseCase.execute(USER_ID, PAGE, PAGE_SIZE, SITE))
            .thenReturn(Single.just(userReputationItem))

        val userReputationListObserver = viewModel.userReputation.test()
        val errorMessageObserver = viewModel.errorMessage.test()
        val isLoadingObserver = viewModel.isLoading.test()

        viewModel.getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE)

        verify(getUserReputationUseCase).execute(USER_ID, PAGE, PAGE_SIZE, SITE)
        Assert.assertEquals(1, viewModel.disposables.size())

        userReputationListObserver
            .assertHistorySize(1)
            .assertValueHistory(userReputationList)

        errorMessageObserver
            .assertHistorySize(0)

        isLoadingObserver
            .assertHistorySize(2)
            .assertValueHistory(true, false)
    }

    @Test
    fun `should not show progress bar when it is not the first load`() {
        whenever(getUserReputationUseCase.execute(USER_ID, 2, PAGE_SIZE, SITE))
            .thenReturn(Single.just(userReputationItem))
        val isLoadingObserver = viewModel.isLoading.test()

        viewModel.getUserReputation(USER_ID, 2, PAGE_SIZE, SITE)

        verify(getUserReputationUseCase).execute(USER_ID, 2, PAGE_SIZE, SITE)

        isLoadingObserver
            .assertHistorySize(2)
            .assertValueHistory(false, false)
    }

    @Test
    fun `should get error message when get user list failed`() {
        val userListObserver = viewModel.userReputation.test()
        val errorMessageObserver = viewModel.errorMessage.test()
        val isLoadingObserver = viewModel.isLoading.test()
        doReturn(Single.error<Throwable>(Throwable(ERROR_MESSAGE)))
            .whenever(getUserReputationUseCase).execute(USER_ID, PAGE, PAGE_SIZE, SITE)

        viewModel.getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE)

        verify(getUserReputationUseCase).execute(USER_ID, PAGE, PAGE_SIZE, SITE)
        Assert.assertEquals(1, viewModel.disposables.size())

        userListObserver
            .assertHistorySize(0)

        errorMessageObserver
            .assertHistorySize(1)
            .assertValueHistory(ERROR_MESSAGE)

        isLoadingObserver
            .assertHistorySize(2)
            .assertValueHistory(true, false)
    }

    private companion object {
        const val USER_ID = 123
        const val PAGE = 1
        const val PAGE_SIZE = 20
        const val SITE = "stackoverflow"
        const val ERROR_MESSAGE = "error"

        val userReputationList = List(2) {
            UserReputation("type 1", 1212, 1212, 1)
            UserReputation("type 2", 2323, 2323, 2)
        }
        val userReputationItem = UserReputationItems(userReputationList)
    }
}