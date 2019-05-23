package com.hing.githubuserlist.customaction

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

/**
 * Created by HingTang on 2019-05-23.
 */
fun recyclerViewWithId(@IdRes id: Int): Matcher<View> {
    return allOf(isAssignableFrom(RecyclerView::class.java), withId(id), isDisplayed())
}
