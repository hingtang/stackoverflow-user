package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.domain.GetUserListUseCase
import com.hing.stackoverflowuser.domain.GetUserListUseCaseImpl
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
}
