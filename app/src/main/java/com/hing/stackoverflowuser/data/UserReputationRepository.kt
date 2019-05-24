package com.hing.stackoverflowuser.data

import com.hing.stackoverflowuser.data.gateways.ApiGateway
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
interface UserReputationRepository {
    fun getUserReputation(userId: Int, page: Int, pageSize: Int, site: String): Single<UserReputationItems>
}

class UserReputationRepositoryImpl @Inject constructor(
    private val apiGateway: ApiGateway
) : UserReputationRepository {
    override fun getUserReputation(userId: Int, page: Int, pageSize: Int, site: String): Single<UserReputationItems> {
        return apiGateway.getUserReputation(userId, page, pageSize, site)
    }
}
