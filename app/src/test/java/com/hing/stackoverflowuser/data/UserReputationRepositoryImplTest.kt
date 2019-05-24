package com.hing.stackoverflowuser.data

import com.hing.stackoverflowuser.data.gateways.ApiGateway
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * Created by HingTang on 2019-05-24.
 */
class UserReputationRepositoryImplTest {

    private lateinit var apiGateway: ApiGateway
    private lateinit var userReputationRepository: UserReputationRepository

    @Before
    fun setUp() {
        apiGateway = mock()
        userReputationRepository = UserReputationRepositoryImpl(apiGateway)
    }

    @Test
    fun `should return user reputation item when get user reputation success`() {
        whenever(apiGateway.getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE))
            .thenReturn(Single.just(userReputationItem))

        userReputationRepository.getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE).test()
            .assertValue(userReputationItem)

        verify(apiGateway).getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE)
    }

    @Test
    fun `should return error when get user reputation failed`() {
        val error = Throwable(ERROR_MESSAGE)
        whenever(apiGateway.getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE))
            .thenReturn(Single.error(error))

        userReputationRepository.getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE).test()
            .assertError(error)

        verify(apiGateway).getUserReputation(USER_ID, PAGE, PAGE_SIZE, SITE)
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