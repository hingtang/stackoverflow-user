package com.hing.stackoverflowuser.domain

import com.hing.stackoverflowuser.data.UserItems
import com.hing.stackoverflowuser.data.UserRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * Created by HingTang on 2019-05-23.
 */
class GetUserListUseCaseImplTest {
    private lateinit var userRepository: UserRepository
    private lateinit var getUserListUseCase: GetUserListUseCase

    @Before
    fun setUp() {
        userRepository = mock()
        getUserListUseCase = GetUserListUseCaseImpl(userRepository)
    }

    @Test
    fun `should return user item when it execute success`() {
        whenever(userRepository.getUserList(PAGE, PAGE_SIZE, SITE)).thenReturn(Single.just(userItems))

        getUserListUseCase.execute(PAGE, PAGE_SIZE, SITE).test()
            .assertValue(userItems)

        verify(userRepository).getUserList(PAGE, PAGE_SIZE, SITE)
    }

    @Test
    fun `should return error when it execute failed`() {
        whenever(userRepository.getUserList(PAGE, PAGE_SIZE, SITE)).thenReturn(Single.error(error))

        getUserListUseCase.execute(PAGE, PAGE_SIZE, SITE).test()
            .assertError(error)

        verify(userRepository).getUserList(PAGE, PAGE_SIZE, SITE)
    }

    private companion object {
        const val PAGE = 1
        const val PAGE_SIZE = 20
        const val SITE = "stackoverflow"

        val userItems = UserItems(emptyList())
        val error = Throwable("ERROR_MESSAGE")
    }
}
