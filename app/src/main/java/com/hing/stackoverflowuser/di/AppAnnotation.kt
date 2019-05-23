package com.hing.stackoverflowuser.di

import javax.inject.Qualifier

/**
 * Created by HingTang on 2019-05-23.
 */
@Qualifier
annotation class IOScheduler

@Qualifier
annotation class MainScheduler

@Qualifier
annotation class ApplicationContext
