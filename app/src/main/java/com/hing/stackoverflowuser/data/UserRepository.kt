package com.hing.stackoverflowuser.data

import com.hing.stackoverflowuser.data.gateways.ApiGateway
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
interface UserRepository {
    fun getUserList(page: Int, pageSize: Int, site: String): Single<UserItems>
}

class UserRepositoryImpl @Inject constructor(
    private val apiGateway: ApiGateway
) : UserRepository {
    override fun getUserList(page: Int, pageSize: Int, site: String): Single<UserItems> {
        return apiGateway.getUserList(page, pageSize, site)
    }
}
