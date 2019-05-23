package com.hing.stackoverflowuser.utils

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
interface NetworkHelper {
    fun isConnectedToInternet(): Boolean
}

class NetworkHelperImpl @Inject constructor(
    private val context: Context
) : NetworkHelper {
    override fun isConnectedToInternet(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
