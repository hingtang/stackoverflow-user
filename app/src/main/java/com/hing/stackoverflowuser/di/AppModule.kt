package com.hing.stackoverflowuser.di

import android.content.Context
import com.hing.stackoverflowuser.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by HingTang on 2019-05-23.
 */
@Module
class AppModule {
    @Provides
    @Singleton
    @ApplicationContext
    fun applicationContext(app: App): Context {
        return app
    }
}
