package com.hing.stackoverflowuser.di

import com.hing.stackoverflowuser.di.scope.FragmentScope
import com.hing.stackoverflowuser.presenter.userlist.UserListViewModel
import com.hing.stackoverflowuser.presenter.userlist.UserListViewModelImpl
import dagger.Binds
import dagger.Module

/**
 * Created by HingTang on 2019-05-23.
 */
@Module(includes = [CommonModule::class])
interface UserListFragmentModule {
    @Binds
    @FragmentScope
    fun userListViewModel(userListViewModel: UserListViewModelImpl): UserListViewModel
}
