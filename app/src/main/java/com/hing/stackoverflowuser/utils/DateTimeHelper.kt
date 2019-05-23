package com.hing.stackoverflowuser.utils

import android.text.format.DateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
interface DateTimeHelper {
    fun getDateString(timestamp: Long): String
}

class DateTimeHelperImpl @Inject constructor() : DateTimeHelper {
    override fun getDateString(timestamp: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp * 1000
        return DateFormat.format("MM/dd/yyyy", cal).toString()
    }
}
