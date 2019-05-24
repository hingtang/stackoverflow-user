package com.hing.stackoverflowuser.utils

import android.annotation.SuppressLint
import android.os.Build
import android.widget.TextView
import androidx.annotation.StyleRes
import com.hing.stackoverflowuser.R

/**
 * Created by HingTang on 2019-05-24.
 */
fun TextView.setTextAppearanceExt(@StyleRes resources: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextAppearance(resources)
    } else {
        this.setTextAppearance(this.context, resources)
    }
}

@SuppressLint("SetTextI18n")
fun TextView.setReputationChange(reputationChange: Int) {
    if (reputationChange >= 0) {
        this.setTextAppearanceExt(R.style.UpPointText)
        this.text = "+$reputationChange"
    } else {
        this.setTextAppearanceExt(R.style.DownPointText)
        this.text = "$reputationChange"
    }
}
