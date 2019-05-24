package com.hing.stackoverflowuser.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hing.stackoverflowuser.data.User

/**
 * Created by HingTang on 2019-05-24.
 */
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO

    companion object {
        const val DATABASE_NAME = "StackOverFlow Database"
    }
}
