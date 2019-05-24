package com.hing.stackoverflowuser.presenter.userlist

import androidx.lifecycle.MutableLiveData
import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.data.UserItems
import com.hing.stackoverflowuser.di.IOScheduler
import com.hing.stackoverflowuser.di.MainScheduler
import com.hing.stackoverflowuser.domain.BookmarkUserUseCase
import com.hing.stackoverflowuser.domain.GetUserListUseCase
import com.hing.stackoverflowuser.domain.LoadBookmarkedUserUseCase
import com.hing.stackoverflowuser.presenter.base.BaseViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
abstract class UserListViewModel : BaseViewModel() {
    abstract val userList: MutableLiveData<List<User>>

    abstract fun loadUserList(
        page: Int,
        pageSize: Int = 20,
        site: String = "stackoverflow",
        isBookmarkSelected: Boolean
    )

    abstract fun loadBookmarkList()
    abstract fun bookmarkUser(user: User)
    abstract fun getCurrentPage(): Int
}

class UserListViewModelImpl @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
    private val loadBookmarkedUserUseCase: LoadBookmarkedUserUseCase,
    private val bookmarkUserUseCase: BookmarkUserUseCase,
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler
) : UserListViewModel() {
    override val disposables = CompositeDisposable()
    override val errorMessage = MutableLiveData<String>()
    override val isLoading = MutableLiveData<Boolean>()
    override val userList = MutableLiveData<List<User>>()

    private var currentPage: Int = 1
    override fun getCurrentPage() = currentPage

    override fun loadUserList(page: Int, pageSize: Int, site: String, isBookmarkSelected: Boolean) {
        if (isBookmarkSelected) {
            loadBookmarkList()
        } else {
            loadRemoteUserList(page, pageSize, site)
        }
    }

    override fun loadBookmarkList() {
        loadBookmarkedUserUseCase.execute()
            .map { userList ->
                userList.filter { user -> user.isBookmark }
            }
            .doOnSubscribe {
                isLoading.value = true
            }
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe({
                currentPage = 0
                userList.value = it
                isLoading.value = false
            }, {
                errorMessage.value = "${it.message}"
                isLoading.value = false
            }).let { disposables.add(it) }
    }

    override fun bookmarkUser(user: User) {
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
                userList.value = it
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
