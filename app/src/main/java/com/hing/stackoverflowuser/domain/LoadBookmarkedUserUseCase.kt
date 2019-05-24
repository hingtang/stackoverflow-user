package com.hing.stackoverflowuser.domain

import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.data.UserRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-24.
 */
interface LoadBookmarkedUserUseCase {
    fun execute(): Single<List<User>>
}

class LoadBookmarkedUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : LoadBookmarkedUserUseCase {
    override fun execute(): Single<List<User>> {
        return userRepository.loadAllBookmarkedUser()
    }
}
