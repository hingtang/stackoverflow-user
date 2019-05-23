package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.MainActivity
import com.hing.stackoverflowuser.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by HingTang on 2019-05-23.
 */
@Module
interface ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class, MainActivityFragmentBindingModule::class])
    fun mainActivity(): MainActivity
}
