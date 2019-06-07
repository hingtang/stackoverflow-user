package com.hing.stackoverflowuser.di

import androidx.lifecycle.ViewModelProvider
import com.hing.stackoverflowuser.viewmodel.StackOverFlowViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Created by HingTang on 2019-06-07.
 */
@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun stackOverFlowViewModelFactory(factory: StackOverFlowViewModelFactory): ViewModelProvider.Factory
}
