package com.hing.stackoverflowuser.di

import android.app.Activity
import com.hing.stackoverflowuser.di.scope.ActivityScope
import com.hing.stackoverflowuser.presenter.userreputation.UserReputationActivity
import com.hing.stackoverflowuser.presenter.userreputation.UserReputationViewModel
import com.hing.stackoverflowuser.presenter.userreputation.UserReputationViewModelImpl
import dagger.Binds
import dagger.Module

/**
 * Created by HingTang on 2019-05-24.
 */
@Module(includes = [CommonModule::class])
interface UserReputationActivityModule {
    @Binds
    @ActivityScope
    fun activity(userReputationActivity: UserReputationActivity): Activity

    @Binds
    @ActivityScope
    fun userDetailViewModel(userReputationViewModel: UserReputationViewModelImpl): UserReputationViewModel
}
