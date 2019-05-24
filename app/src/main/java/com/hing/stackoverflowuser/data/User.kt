package com.hing.stackoverflowuser.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by HingTang on 2019-05-23.
 */
@Entity
data class User(
    @PrimaryKey
    @SerializedName("account_id")
    val id: Int,

    @SerializedName("display_name")
    val username: String = "",

    @SerializedName("profile_image")
    val avatarUrl: String = "",

    @SerializedName("last_access_date")
    val lastAccessDate: Long,

    val reputation: Int,
    val location: String = "",
    var isBookmark: Boolean = false
)
