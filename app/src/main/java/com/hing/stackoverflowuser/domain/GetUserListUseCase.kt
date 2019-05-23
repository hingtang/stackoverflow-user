package com.hing.stackoverflowuser.domain

import com.hing.stackoverflowuser.data.UserItems
import com.hing.stackoverflowuser.data.UserRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
interface GetUserListUseCase {
    fun execute(page: Int, pageSize: Int, site: String): Single<UserItems>
}

class GetUserListUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserListUseCase {
    override fun execute(page: Int, pageSize: Int, site: String): Single<UserItems> {
        return userRepository.getUserList(page, pageSize, site)
    }
}
