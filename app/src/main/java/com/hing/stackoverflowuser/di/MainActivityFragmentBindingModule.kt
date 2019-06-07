package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.di.scope.FragmentScope
import com.hing.stackoverflowuser.ui.userlist.UserListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by HingTang on 2019-05-23.
 */
@Module
interface MainActivityFragmentBindingModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [UserListFragmentModule::class])
    fun userListFragment(): UserListFragment
}
