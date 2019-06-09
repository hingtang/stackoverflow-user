package com.hing.stackoverflowuser.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hing.stackoverflowuser.testing.OpenForTesting
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by HingTang on 2019-05-23.
 */
@OpenForTesting
abstract class BaseViewModel : ViewModel() {
    abstract val disposables: CompositeDisposable
    abstract val errorMessage: LiveData<String>
    abstract val isLoading: LiveData<Boolean>

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
