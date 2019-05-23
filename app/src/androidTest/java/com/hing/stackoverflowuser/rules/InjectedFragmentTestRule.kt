package com.hing.stackoverflowuser.rules

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.hing.stackoverflowuser.TestHelperActivity
import dagger.android.AndroidInjector

/**
 * Created by HingTang on 2019-05-23.
 */
class InjectedFragmentTestRule<T : Fragment>(
    private val fragmentInjector: (T) -> Unit
) : IntentsTestRule<TestHelperActivity>(TestHelperActivity::class.java, false, false) {

    lateinit var fragment: T

    override fun afterActivityLaunched() {
        super.afterActivityLaunched()
        activity.fragmentInjector = AndroidInjector {
            @Suppress("UNCHECKED_CAST")
            fragmentInjector(it as T)
        }
    }

    fun launchFragment(fragment: T) {
        super.launchActivity(Intent())
        attachFragment(fragment)
    }

    private fun attachFragment(fragment: T) {
        this.fragment = fragment
        activity.attachFragment(fragment)
    }
}
