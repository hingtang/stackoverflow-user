package com.hing.stackoverflowuser.data

import com.hing.stackoverflowuser.data.gateways.ApiGateway
import com.hing.stackoverflowuser.data.gateways.DatabaseGateway
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
interface UserRepository {
    fun getUserList(page: Int, pageSize: Int, site: String): Single<UserItems>
    fun bookmarkUser(user: User): Completable
    fun loadAllBookmarkedUser(): Single<List<User>>
}

class UserRepositoryImpl @Inject constructor(
    private val apiGateway: ApiGateway,
    private val databaseGateway: DatabaseGateway
) : UserRepository {
    override fun getUserList(page: Int, pageSize: Int, site: String): Single<UserItems> {
        return apiGateway.getUserList(page, pageSize, site)
    }

    override fun bookmarkUser(user: User): Completable {
        return databaseGateway.bookmarkUser(user)
    }

    override fun loadAllBookmarkedUser(): Single<List<User>> {
        return databaseGateway.loadAllBookmarkedUser()
    }
}
