package com.hing.stackoverflowuser.domain

import com.hing.stackoverflowuser.data.UserReputationItems
import com.hing.stackoverflowuser.data.UserReputationRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
interface GetUserReputationUseCase {
    fun execute(userId: Int, page: Int, pageSize: Int, site: String): Single<UserReputationItems>
}

class GetUserReputationUseCaseImpl @Inject constructor(
    private val userReputationRepository: UserReputationRepository
) : GetUserReputationUseCase {
    override fun execute(userId: Int, page: Int, pageSize: Int, site: String): Single<UserReputationItems> {
        return userReputationRepository.getUserReputation(userId, page, pageSize, site)
    }
}
