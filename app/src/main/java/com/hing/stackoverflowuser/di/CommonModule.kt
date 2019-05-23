package com.hing.stackoverflowuser.di

import android.content.Context
import com.hing.stackoverflowuser.utils.DateTimeHelper
import com.hing.stackoverflowuser.utils.DateTimeHelperImpl
import com.hing.stackoverflowuser.utils.NetworkHelper
import com.hing.stackoverflowuser.utils.NetworkHelperImpl
import dagger.Module
import dagger.Provides

/**
 * Created by HingTang on 2019-05-23.
 */
@Module
class CommonModule {
    @Provides
    fun networkHelper(@ApplicationContext context: Context): NetworkHelper = NetworkHelperImpl(context)

    @Provides
    fun dateTimeHelper(): DateTimeHelper = DateTimeHelperImpl()
}
