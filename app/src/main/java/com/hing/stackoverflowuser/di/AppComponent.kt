package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by HingTang on 2019-05-23.
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelFactoryModule::class,
        DomainModule::class,
        AppModule::class,
        GlobalModule::class,
        NetworkModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    abstract class Builder : AndroidInjector.Factory<App>
}
