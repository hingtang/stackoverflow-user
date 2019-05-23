package com.hing.stackoverflowuser.data

import com.hing.stackoverflowuser.data.gateways.ApiGateway
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * Created by HingTang on 2019-05-23.
 */
class UserRepositoryImplTest {
    private lateinit var userRepository: UserRepository
    private lateinit var apiGateway: ApiGateway

    @Before
    fun setUp() {
        apiGateway = mock()
        userRepository = UserRepositoryImpl(apiGateway)
    }

    @Test
    fun `should return user item when it execute success`() {
        whenever(apiGateway.getUserList(PAGE, PAGE_SIZE, SITE)).thenReturn(Single.just(userItems))

        userRepository.getUserList(PAGE, PAGE_SIZE, SITE).test()
            .assertValue(userItems)

        verify(apiGateway).getUserList(PAGE, PAGE_SIZE, SITE)
    }

    @Test
    fun `should return error when it execute failed`() {
        whenever(apiGateway.getUserList(PAGE, PAGE_SIZE, SITE)).thenReturn(Single.error(error))

        userRepository.getUserList(PAGE, PAGE_SIZE, SITE).test()
            .assertError(error)

        verify(apiGateway).getUserList(PAGE, PAGE_SIZE, SITE)
    }

    private companion object {
        const val PAGE = 1
        const val PAGE_SIZE = 20
        const val SITE = "stackoverflow"

        val userItems = UserItems(emptyList())
        val error = Throwable("ERROR_MESSAGE")
    }
}
