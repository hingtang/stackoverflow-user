package com.hing.stackoverflowuser.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * Created by HingTang on 2019-05-24.
 */
fun ImageView.setCustomImage(condition: Boolean, @DrawableRes trueResources: Int, @DrawableRes falseResources: Int) {
    this.setImageResource(if (condition) trueResources else falseResources)
}
