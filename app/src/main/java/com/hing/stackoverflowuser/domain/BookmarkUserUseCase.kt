package com.hing.stackoverflowuser.domain

import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.data.UserRepository
import io.reactivex.Completable
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-24.
 */
interface BookmarkUserUseCase {
    fun execute(user: User): Completable
}

class BookmarkUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : BookmarkUserUseCase {
    override fun execute(user: User): Completable {
        return userRepository.bookmarkUser(user)
    }
}
