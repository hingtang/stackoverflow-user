package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.data.gateways.ApiGateway
import com.hing.stackoverflowuser.data.gateways.ApiGatewayImpl
import dagger.Binds
import dagger.Module

/**
 * Created by HingTang on 2019-05-23.
 */
@Module(includes = [ApiModule::class])
interface GatewayModule {
    @Binds
    fun apiGateway(apiGateway: ApiGatewayImpl): ApiGateway
}
