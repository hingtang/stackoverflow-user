package com.hing.stackoverflowuser.presenter.userlist

import androidx.lifecycle.MutableLiveData
import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.di.IOScheduler
import com.hing.stackoverflowuser.di.MainScheduler
import com.hing.stackoverflowuser.domain.GetUserListUseCase
import com.hing.stackoverflowuser.presenter.base.BaseViewModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
abstract class UserListViewModel : BaseViewModel() {
    abstract val userList: MutableLiveData<List<User>>

    abstract fun loadUserList(page: Int, pageSize: Int = 20, site: String = "stackoverflow")
}

class UserListViewModelImpl @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler
) : UserListViewModel() {
    override val disposables = CompositeDisposable()
    override val errorMessage = MutableLiveData<String>()
    override val isLoading = MutableLiveData<Boolean>()
    override val userList = MutableLiveData<List<User>>()

    override fun loadUserList(page: Int, pageSize: Int, site: String) {
        getUserListUseCase.execute(page, pageSize, site)
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .doOnSubscribe {
                isLoading.value = page == 1
            }
            .subscribe({
                userList.value = it.items
                isLoading.value = false
            }, {
                errorMessage.value = "${it.message}"
                isLoading.value = false
            }).let { disposables.add(it) }
    }
}
