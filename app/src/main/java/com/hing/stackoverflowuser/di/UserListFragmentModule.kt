package com.hing.stackoverflowuser.di

import androidx.lifecycle.ViewModel
import com.hing.stackoverflowuser.ui.userlist.UserListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HingTang on 2019-05-23.
 */
@Module(includes = [CommonModule::class])
interface UserListFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    fun userListViewModel(userListViewModel: UserListViewModel): ViewModel
}
