package com.hing.stackoverflowuser.data.db

import androidx.room.*
import com.hing.stackoverflowuser.data.User
import io.reactivex.Completable
import io.reactivex.Maybe

/**
 * Created by HingTang on 2019-05-24.
 */
@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bookmarkUser(user: User): Completable

    @Query("SELECT * FROM user")
    fun loadAllBookmarkedUser(): Maybe<List<User>>
}
