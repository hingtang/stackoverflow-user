package com.hing.stackoverflowuser.ui.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.data.UserItems
import com.hing.stackoverflowuser.di.IOScheduler
import com.hing.stackoverflowuser.di.MainScheduler
import com.hing.stackoverflowuser.domain.BookmarkUserUseCase
import com.hing.stackoverflowuser.domain.GetUserListUseCase
import com.hing.stackoverflowuser.domain.LoadBookmarkedUserUseCase
import com.hing.stackoverflowuser.ui.base.BaseViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
class UserListViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
    private val loadBookmarkedUserUseCase: LoadBookmarkedUserUseCase,
    private val bookmarkUserUseCase: BookmarkUserUseCase,
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler
) : BaseViewModel() {
    override val disposables = CompositeDisposable()
    override val errorMessage = MutableLiveData<String>()
    override val isLoading = MutableLiveData<Boolean>()

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get() = _userList

    private var currentPage: Int = 1
    fun getCurrentPage() = currentPage

    fun loadUserList(
        page: Int,
        pageSize: Int = 20,
        site: String = "stackoverflow",
        isBookmarkSelected: Boolean
    ) {
        if (isBookmarkSelected) {
            loadBookmarkList()
        } else {
            loadRemoteUserList(page, pageSize, site)
        }
    }

    fun loadBookmarkList() {
        loadBookmarkedUserUseCase.execute()
            .map { userList ->
                userList.filter { user -> user.isBookmark }
            }
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .doOnSubscribe {
                isLoading.value = true
            }
            .subscribe({
                currentPage = 0
                _userList.value = it
                isLoading.value = false
            }, {
                errorMessage.value = "${it.message}"
                isLoading.value = false
            }).let { disposables.add(it) }
    }

    fun bookmarkUser(user: User) {
        bookmarkUserUseCase.execute(user)
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe({
                //Ignored
            }, {
                errorMessage.value = "${it.message}"
            }).let { disposables.add(it) }
    }

    private fun loadRemoteUserList(page: Int, pageSize: Int, site: String) {
        Single.zip(
            getUserListUseCase.execute(page, pageSize, site),
            loadBookmarkedUserUseCase.execute(),
            BiFunction<UserItems, List<User>, List<User>> { remoteUserList, localUserList ->
                updateUserBookmark(remoteUserList.items, localUserList)
            }
        ).subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .doOnSubscribe {
                isLoading.value = page == 1
            }
            .subscribe({
                currentPage = page
                _userList.value = it
                isLoading.value = false
            }, {
                errorMessage.value = "${it.message}"
                isLoading.value = false
            }).let { disposables.add(it) }
    }

    private fun updateUserBookmark(remoteUserList: List<User>, localUserList: List<User>): List<User> {
        if (remoteUserList.isEmpty()) {
            return localUserList
        }
        for (user in localUserList) {
            remoteUserList.firstOrNull { it.id == user.id }?.isBookmark = user.isBookmark
        }
        return remoteUserList
    }
}
