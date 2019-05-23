package com.hing.stackoverflowuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hing.stackoverflowuser.presenter.userlist.UserListFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container_layout, UserListFragment(), UserListFragment::class.java.simpleName).commit()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        if (::dispatchingAndroidInjector.isInitialized) {
            return dispatchingAndroidInjector
        }
        return AndroidInjector { }
    }
}
