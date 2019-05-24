package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.data.UserRepository
import com.hing.stackoverflowuser.data.UserRepositoryImpl
import com.hing.stackoverflowuser.data.UserReputationRepository
import com.hing.stackoverflowuser.data.UserReputationRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by HingTang on 2019-05-23.
 */
@Module(includes = [GatewayModule::class])
interface RepositoryModule {
    @Binds
    fun userRepository(userRepository: UserRepositoryImpl): UserRepository

    @Binds
    fun userReputationRepository(userReputationRepository: UserReputationRepositoryImpl): UserReputationRepository
}
