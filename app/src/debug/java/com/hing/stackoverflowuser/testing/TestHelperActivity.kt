package com.hing.stackoverflowuser.testing

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by HingTang on 2019-05-23.
 */
class TestHelperActivity : AppCompatActivity(), HasSupportFragmentInjector {

    lateinit var fragmentInjector: AndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    fun attachFragment(fragment: Fragment) {
        runBlockingUiThread {
            supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()
        }
    }

    private fun runBlockingUiThread(block: () -> Unit) {
        val latch = CountDownLatch(1)
        runOnUiThread {
            block()
            latch.countDown()
        }
        latch.await(200, TimeUnit.MILLISECONDS)
    }
}
