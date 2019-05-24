package com.hing.stackoverflowuser.rules

import android.app.Activity
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.hing.stackoverflowuser.AppTest
import dagger.android.AndroidInjector

/**
 * Created by HingTang on 2019-05-24.
 */
class InjectedActivityTestRule<T : Activity>(
    activityClass: Class<T>,
    private val activityInjector: (T) -> Unit
) : IntentsTestRule<T>(activityClass, false, true) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        ApplicationProvider.getApplicationContext<AppTest>().apply {
            activityInjector = AndroidInjector { instance ->
                @Suppress("unchecked_cast")
                activityInjector(instance as T)
            }
        }
    }
}
