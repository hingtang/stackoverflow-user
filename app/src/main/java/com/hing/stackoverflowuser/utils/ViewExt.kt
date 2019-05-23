package com.hing.stackoverflowuser.utils

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

/**
 * Created by HingTang on 2019-05-23.
 */
fun View.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}
