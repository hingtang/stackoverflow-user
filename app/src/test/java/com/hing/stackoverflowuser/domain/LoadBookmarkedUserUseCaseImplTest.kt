package com.hing.stackoverflowuser.domain

import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.data.UserRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * Created by HingTang on 2019-05-24.
 */
class LoadBookmarkedUserUseCaseImplTest {
    private lateinit var userRepository: UserRepository
    private lateinit var loadBookmarkedUserUseCase: LoadBookmarkedUserUseCase

    @Before
    fun setUp() {
        userRepository = mock()
        loadBookmarkedUserUseCase = LoadBookmarkedUserUseCaseImpl(userRepository)
    }

    @Test
    fun `should return bookmarked user list when it execute success`() {
        whenever(userRepository.loadAllBookmarkedUser()).thenReturn(Single.just(userList))

        loadBookmarkedUserUseCase.execute().test()
            .assertValue(userList)

        verify(userRepository).loadAllBookmarkedUser()
    }

    @Test
    fun `should return error when it execute failed`() {
        whenever(userRepository.loadAllBookmarkedUser()).thenReturn(Single.error(error))

        loadBookmarkedUserUseCase.execute().test()
            .assertError(error)

        verify(userRepository).loadAllBookmarkedUser()
    }

    private companion object {
        val userList = List(2) {
            User(0, lastAccessDate = 0, reputation = 0)
            User(1, lastAccessDate = 0, reputation = 0)
        }
        val error = Throwable("ERROR_MESSAGE")
    }
}
