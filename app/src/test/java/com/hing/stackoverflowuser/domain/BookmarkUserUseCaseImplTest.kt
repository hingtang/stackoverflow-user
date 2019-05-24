package com.hing.stackoverflowuser.domain

import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.data.UserRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test

/**
 * Created by HingTang on 2019-05-24.
 */
class BookmarkUserUseCaseImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var bookmarkUserUseCase: BookmarkUserUseCase

    @Before
    fun setUp() {
        userRepository = mock()
        bookmarkUserUseCase = BookmarkUserUseCaseImpl(userRepository)
    }

    @Test
    fun `should return completed when it execute success`() {
        whenever(userRepository.bookmarkUser(user)).thenReturn(Completable.complete())

        bookmarkUserUseCase.execute(user).test()
            .assertComplete()

        verify(userRepository).bookmarkUser(user)
    }

    @Test
    fun `should return error when it execute failed`() {
        whenever(userRepository.bookmarkUser(user)).thenReturn(Completable.error(error))

        bookmarkUserUseCase.execute(user).test()
            .assertNotComplete()
            .assertError(error)

        verify(userRepository).bookmarkUser(user)
    }

    private companion object {
        val user = User(1, lastAccessDate = 1, reputation = 1)
        val error = Throwable("ERROR_MESSAGE")
    }
}
