package com.hing.stackoverflowuser.domain

import com.hing.stackoverflowuser.data.UserReputationItems
import com.hing.stackoverflowuser.data.UserReputationRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * Created by HingTang on 2019-05-24.
 */
class GetUserReputationUseCaseImplTest {

    private lateinit var userReputationRepository: UserReputationRepository
    private lateinit var getUserReputationUseCase: GetUserReputationUseCase

    @Before
    fun setUp() {
        userReputationRepository = mock()
        getUserReputationUseCase = GetUserReputationUseCaseImpl(userReputationRepository)
    }

    @Test
    fun `should return user reputation item when get user reputation success`() {
        whenever(userReputationRepository.getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE))
            .thenReturn(Single.just(userReputationItem))

        getUserReputationUseCase.execute(USER_ID, PAGE, PAGE_SIZE, SITE).test()
            .assertValue(userReputationItem)

        verify(userReputationRepository).getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE)
    }

    @Test
    fun `should return error when get user reputation failed`() {
        val error = Throwable(ERROR_MESSAGE)
        whenever(userReputationRepository.getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE))
            .thenReturn(Single.error(error))

        getUserReputationUseCase.execute(USER_ID, PAGE, PAGE_SIZE, SITE).test()
            .assertError(error)

        verify(userReputationRepository).getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE)
    }

    private companion object {
        const val USER_ID = 123
        const val PAGE = 1
        const val PAGE_SIZE = 20
        const val SITE = "stackoverflow"
        const val ERROR_MESSAGE = "error"

        val userReputationItem = UserReputationItems(emptyList())
    }
}