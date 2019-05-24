package com.hing.stackoverflowuser.presenter.userreputation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hing.stackoverflowuser.R
import com.hing.stackoverflowuser.data.UserReputation
import com.hing.stackoverflowuser.utils.DateTimeHelper
import com.hing.stackoverflowuser.utils.setReputationChange

/**
 * Created by HingTang on 2019-05-23.
 */
class UserReputationAdapter(
    private val dateTimeHelper: DateTimeHelper
) : RecyclerView.Adapter<UserReputationAdapter.UserReputationViewHolder>() {
    private val userList: MutableList<UserReputation> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReputationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_reputation, parent, false)
        return UserReputationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserReputationViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    fun updateUserReputation(users: List<UserReputation>) {
        if (userList.containsAll(users)) {
            userList.clear()
        }
        userList.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserReputationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postIdText = lazy { itemView.findViewById<TextView>(R.id.tv_post_id) }
        private val creationDateText = lazy { itemView.findViewById<TextView>(R.id.tv_creation_date) }
        private val reputationHistoryTypeText =
            lazy { itemView.findViewById<TextView>(R.id.tv_reputation_history_type) }
        private val reputationChangeText = lazy { itemView.findViewById<TextView>(R.id.tv_reputation_change) }

        @SuppressLint("SetTextI18n")
        fun bind(userReputation: UserReputation) {
            postIdText.value.text = "Post ID: ${userReputation.postId}"
            creationDateText.value.text = dateTimeHelper.getDateString(userReputation.createAt)
            reputationHistoryTypeText.value.text = userReputation.reputationType
            reputationChangeText.value.setReputationChange(userReputation.change)
        }
    }
}
