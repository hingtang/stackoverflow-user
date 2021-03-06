package com.hing.stackoverflowuser.ui.userlist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hing.stackoverflowuser.R
import com.hing.stackoverflowuser.data.User
import com.hing.stackoverflowuser.utils.DateTimeHelper
import com.hing.stackoverflowuser.utils.setCustomImage
import com.hing.stackoverflowuser.utils.setVisible


/**
 * Created by HingTang on 2019-05-23.
 */
class UserListAdapter(
    private val onItemClick: (userId: Int) -> Unit,
    private val onBookmarkClick: (user: User) -> Unit,
    private val dateTimeHelper: DateTimeHelper
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    private val userList: MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    fun updateUserList(users: List<User>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun insertUserList(users: List<User>) {
        userList.addAll(users)
        Log.e("Hing", "startIndex: ${userList.size - users.size} - currentSize: ${userList.size} - size: ${users.size}")
        notifyItemRangeInserted(userList.size - users.size, users.size)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarImage = lazy { itemView.findViewById<ImageView>(R.id.imgv_avatar) }
        private val usernameText = lazy { itemView.findViewById<TextView>(R.id.tv_username) }
        private val reputationText = lazy { itemView.findViewById<TextView>(R.id.tv_reputation) }
        private val locationText = lazy { itemView.findViewById<TextView>(R.id.tv_location) }
        private val lastAccessDateText = lazy { itemView.findViewById<TextView>(R.id.tv_last_access_date) }
        private val bookmarkImage = lazy { itemView.findViewById<ImageView>(R.id.bookmark) }

        init {
            itemView.setOnClickListener { onItemClick(userList[adapterPosition].id) }
            bookmarkImage.value.setOnClickListener {
                val user = userList[adapterPosition].apply { isBookmark = !isBookmark }
                bookmarkImage.value.setCustomImage(user.isBookmark, R.drawable.ic_star, R.drawable.ic_un_star)
                onBookmarkClick(user)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .placeholder(R.drawable.ic_no_image)
                .apply(RequestOptions.circleCropTransform())
                .into(avatarImage.value)

            usernameText.value.text = user.username
            reputationText.value.text = "${user.reputation}"
            locationText.value.text = user.location
            lastAccessDateText.value.text = dateTimeHelper.getDateString(user.lastAccessDate)
            bookmarkImage.value.setCustomImage(user.isBookmark, R.drawable.ic_star, R.drawable.ic_un_star)

            locationText.value.setVisible(!user.location.isNullOrEmpty())
        }
    }
}
