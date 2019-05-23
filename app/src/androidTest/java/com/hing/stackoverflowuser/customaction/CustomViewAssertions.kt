package com.hing.githubuserlist.customaction

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion

/**
 * Created by HingTang on 2019-05-23.
 */
fun recyclerViewItemCount(count: Int): ViewAssertion {
    return ViewAssertion { view, _ ->
        view is RecyclerView && view.adapter?.itemCount == count
    }
}
