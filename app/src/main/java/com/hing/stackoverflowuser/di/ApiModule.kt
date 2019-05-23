package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.data.StackOverFlowApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by HingTang on 2019-05-23.
 */
@Module
class ApiModule {
    @Provides
    fun stackOverFlowApi(retrofit: Retrofit) = retrofit.create(StackOverFlowApi::class.java)
}
