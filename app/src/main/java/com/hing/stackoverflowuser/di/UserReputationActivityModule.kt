package com.hing.stackoverflowuser.di

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.hing.stackoverflowuser.di.scope.ActivityScope
import com.hing.stackoverflowuser.ui.userreputation.UserReputationActivity
import com.hing.stackoverflowuser.ui.userreputation.UserReputationViewModel
import com.hing.stackoverflowuser.ui.userreputation.UserReputationViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HingTang on 2019-05-24.
 */
@Module(includes = [CommonModule::class])
interface UserReputationActivityModule {
    @Binds
    @ActivityScope
    fun activity(userReputationActivity: UserReputationActivity): Activity

    @Binds
    @IntoMap
    @ViewModelKey(UserReputationViewModel::class)
    fun userDetailViewModel(userReputationViewModel: UserReputationViewModelImpl): ViewModel
}
