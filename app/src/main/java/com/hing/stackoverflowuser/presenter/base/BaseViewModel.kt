package com.hing.stackoverflowuser.presenter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by HingTang on 2019-05-23.
 */
abstract class BaseViewModel : ViewModel() {
    abstract val disposables: CompositeDisposable
    abstract val errorMessage: LiveData<String>
    abstract val isLoading: LiveData<Boolean>

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
