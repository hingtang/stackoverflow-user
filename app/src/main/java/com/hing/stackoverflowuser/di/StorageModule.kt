package com.hing.stackoverflowuser.di

import android.content.Context
import androidx.room.Room
import com.hing.stackoverflowuser.data.db.AppDatabase
import com.hing.stackoverflowuser.data.db.AppDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by HingTang on 2019-05-24.
 */
@Module
class StorageModule {
    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, DATABASE_NAME
        ).build()
    }
}
