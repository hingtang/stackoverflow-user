package com.hing.stackoverflowuser.data

import com.google.gson.annotations.SerializedName

/**
 * Created by HingTang on 2019-05-23.
 */
class UserReputation(
    @SerializedName("reputation_history_type")
    val reputationType: String,

    @SerializedName("reputation_change")
    val change: Int,

    @SerializedName("creation_date")
    val createAt: Long,

    @SerializedName("post_id")
    val postId: Int
)
