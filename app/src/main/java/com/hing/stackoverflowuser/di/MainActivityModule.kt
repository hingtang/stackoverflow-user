package com.hing.stackoverflowuser.di

import android.app.Activity
import com.hing.stackoverflowuser.MainActivity
import com.hing.stackoverflowuser.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

/**
 * Created by HingTang on 2019-05-23.
 */
@Module
interface MainActivityModule {
    @Binds
    @ActivityScope
    fun activity(activity: MainActivity): Activity
}
