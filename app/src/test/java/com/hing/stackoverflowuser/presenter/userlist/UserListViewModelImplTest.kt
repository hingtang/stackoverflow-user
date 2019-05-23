package com.hing.stackoverflowuser.presenter.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.data.UserItems
import com.hing.stackoverflowuser.domain.GetUserListUseCase
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
 * Created by HingTang on 2019-05-23.
 */
class UserListViewModelImplTest {

    private lateinit var getUserListUseCase: GetUserListUseCase
    private lateinit var viewModel: UserListViewModel

    @get:Rule
    val instantExecutorRole = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        getUserListUseCase = mock()
        viewModel = UserListViewModelImpl(getUserListUseCase, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    fun `should get the correct value when get user list success`() {
        whenever(getUserListUseCase.execute(PAGE, PAGE_SIZE, SITE)).thenReturn(Single.just(userItems))

        val userListObserver = viewModel.userList.test()
        val errorMessageObserver = viewModel.errorMessage.test()
        val isLoadingObserver = viewModel.isLoading.test()

        viewModel.loadUserList(PAGE)

        verify(getUserListUseCase).execute(PAGE, PAGE_SIZE, SITE)
        Assert.assertEquals(1, viewModel.disposables.size())

        userListObserver
            .assertHistorySize(1)
            .assertValueHistory(userList)

        errorMessageObserver
            .assertHistorySize(0)

        isLoadingObserver
            .assertHistorySize(2)
            .assertValueHistory(true, false)
    }

    @Test
    fun `should not show progress bar when it is not the first load`() {
        whenever(getUserListUseCase.execute(2, PAGE_SIZE, SITE)).thenReturn(Single.just(userItems))
        val isLoadingObserver = viewModel.isLoading.test()

        viewModel.loadUserList(2)

        verify(getUserListUseCase).execute(2, PAGE_SIZE, SITE)

        isLoadingObserver
            .assertHistorySize(2)
            .assertValueHistory(false, false)
    }

    @Test
    fun `should get error message when get user list failed`() {
        val userListObserver = viewModel.userList.test()
        val errorMessageObserver = viewModel.errorMessage.test()
        val isLoadingObserver = viewModel.isLoading.test()
        doReturn(Single.error<Throwable>(Throwable(errorMessage))).whenever(getUserListUseCase)
            .execute(PAGE, PAGE_SIZE, SITE)

        viewModel.loadUserList(PAGE, PAGE_SIZE, SITE)

        verify(getUserListUseCase).execute(PAGE, PAGE_SIZE, SITE)
        Assert.assertEquals(1, viewModel.disposables.size())

        userListObserver
            .assertHistorySize(0)

        errorMessageObserver
            .assertHistorySize(1)
            .assertValueHistory(errorMessage)

        isLoadingObserver
            .assertHistorySize(2)
            .assertValueHistory(true, false)
    }

    private companion object {
        const val PAGE = 1
        const val PAGE_SIZE = 20
        const val SITE = "stackoverflow"
        const val errorMessage = "ERROR_MESSAGE"

        val userList = List(2) {
            User(0, lastAccessDate = 0, reputation = 0)
            User(1, lastAccessDate = 0, reputation = 0)
        }
        val userItems = UserItems(userList)
    }
}
