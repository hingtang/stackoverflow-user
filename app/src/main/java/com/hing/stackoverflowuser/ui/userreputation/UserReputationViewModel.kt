package com.hing.stackoverflowuser.ui.userreputation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hing.stackoverflowuser.data.UserReputation
import com.hing.stackoverflowuser.di.IOScheduler
import com.hing.stackoverflowuser.di.MainScheduler
import com.hing.stackoverflowuser.domain.GetUserReputationUseCase
import com.hing.stackoverflowuser.ui.base.BaseViewModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
abstract class UserReputationViewModel : BaseViewModel() {
    abstract val userReputation: LiveData<List<UserReputation>>

    abstract fun getUserReputation(userId: Int, page: Int, pageSize: Int = 20, site: String = "stackoverflow")
}

class UserReputationViewModelImpl @Inject constructor(
    private val getUserReputationUseCase: GetUserReputationUseCase,
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler
) : UserReputationViewModel() {
    override val disposables = CompositeDisposable()
    override val errorMessage = MutableLiveData<String>()
    override val isLoading = MutableLiveData<Boolean>()
    override val userReputation = MutableLiveData<List<UserReputation>>()

    override fun getUserReputation(userId: Int, page: Int, pageSize: Int, site: String) {
        getUserReputationUseCase.execute(userId, page, pageSize, site)
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .doOnSubscribe {
                isLoading.value = page == 1
            }
            .subscribe({
                userReputation.value = it.items
                isLoading.value = false
            }, {
                errorMessage.value = "${it.message}"
                isLoading.value = false
            }).let { disposables.add(it) }
    }
}
