package com.hing.stackoverflowuser

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector

/**
 * Created by HingTang on 2019-05-23.
 */
class AppTest : Application(), HasActivityInjector {
    var activityInjector = AndroidInjector<Activity> {
        //Do nothing
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}
