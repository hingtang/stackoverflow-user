package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.domain.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by HingTang on 2019-05-23.
 */
@Module(includes = [RepositoryModule::class])
interface DomainModule {
    @Binds
    @Singleton
    fun getUserListUseCase(getUserListUseCase: GetUserListUseCaseImpl): GetUserListUseCase

    @Binds
    @Singleton
    fun getUserReputationUseCase(getUserReputationUseCase: GetUserReputationUseCaseImpl): GetUserReputationUseCase

    @Binds
    @Singleton
    fun bookmarUserUseCase(bookmarkedUserUseCase: BookmarkUserUseCaseImpl): BookmarkUserUseCase

    @Binds
    @Singleton
    fun loadBookmarkedUserUseCase(loadBookmarkedUserUseCase: LoadBookmarkedUserUseCaseImpl): LoadBookmarkedUserUseCase
}
