package com.hing.stackoverflowuser.data.gateways

import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.data.db.AppDatabase
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-24.
 */
interface DatabaseGateway {
    fun bookmarkUser(user: User): Completable
    fun loadAllBookmarkedUser(): Single<List<User>>
}

class DatabaseGatewayImpl @Inject constructor(
    private val database: AppDatabase
) : DatabaseGateway {
    override fun bookmarkUser(user: User): Completable {
        return database.userDao().bookmarkUser(user)
    }

    override fun loadAllBookmarkedUser(): Single<List<User>> {
        return database.userDao().loadAllBookmarkedUser().toSingle(emptyList())
    }
}
