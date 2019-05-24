package com.hing.stackoverflowuser.data

import com.hing.stackoverflowuser.data.gateways.ApiGateway
import com.hing.stackoverflowuser.data.gateways.DatabaseGateway
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * Created by HingTang on 2019-05-23.
 */
class UserRepositoryImplTest {
    private lateinit var userRepository: UserRepository
    private lateinit var apiGateway: ApiGateway
    private lateinit var databaseGateway: DatabaseGateway

    @Before
    fun setUp() {
        apiGateway = mock()
        databaseGateway = mock()
        userRepository = UserRepositoryImpl(apiGateway, databaseGateway)
    }

    @Test
    fun `should return user item when getUserList success`() {
        whenever(apiGateway.getUserList(PAGE, PAGE_SIZE, SITE)).thenReturn(Single.just(userItems))

        userRepository.getUserList(PAGE, PAGE_SIZE, SITE).test()
            .assertValue(userItems)

        verify(apiGateway).getUserList(PAGE, PAGE_SIZE, SITE)
    }

    @Test
    fun `should return error when getUserList failed`() {
        whenever(apiGateway.getUserList(PAGE, PAGE_SIZE, SITE)).thenReturn(Single.error(error))

        userRepository.getUserList(PAGE, PAGE_SIZE, SITE).test()
            .assertError(error)

        verify(apiGateway).getUserList(PAGE, PAGE_SIZE, SITE)
    }

    @Test
    fun `should return completed when bookmarkUser success`() {
        whenever(databaseGateway.bookmarkUser(user)).thenReturn(Completable.complete())

        userRepository.bookmarkUser(user).test()
            .assertComplete()

        verify(databaseGateway).bookmarkUser(user)
    }

    @Test
    fun `should return error when bookmarkUser failed`() {
        whenever(databaseGateway.bookmarkUser(user)).thenReturn(Completable.error(error))

        userRepository.bookmarkUser(user).test()
            .assertNotComplete()
            .assertError(error)

        verify(databaseGateway).bookmarkUser(user)
    }

    @Test
    fun `should return bookmarked user item when loadAllBookmarkedUser success`() {
        whenever(databaseGateway.loadAllBookmarkedUser()).thenReturn(Single.just(userList))

        userRepository.loadAllBookmarkedUser().test()
            .assertValue(userList)

        verify(databaseGateway).loadAllBookmarkedUser()
    }

    @Test
    fun `should return error when getUserList execute failed`() {
        whenever(databaseGateway.loadAllBookmarkedUser()).thenReturn(Single.error(error))

        userRepository.loadAllBookmarkedUser().test()
            .assertError(error)

        verify(databaseGateway).loadAllBookmarkedUser()
    }

    private companion object {
        const val PAGE = 1
        const val PAGE_SIZE = 20
        const val SITE = "stackoverflow"

        val userItems = UserItems(emptyList())
        val userList = List(2) {
            User(0, lastAccessDate = 0, reputation = 0)
            User(1, lastAccessDate = 0, reputation = 0)
        }
        val user = User(5, lastAccessDate = 5, reputation = 5)
        val error = Throwable("ERROR_MESSAGE")
    }
}
