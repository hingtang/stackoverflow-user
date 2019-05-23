package com.hing.stackoverflowuser.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by HingTang on 2019-05-23.
 */
@Module
class GlobalModule {
    @Provides
    @IOScheduler
    fun ioScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @MainScheduler
    fun mainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
