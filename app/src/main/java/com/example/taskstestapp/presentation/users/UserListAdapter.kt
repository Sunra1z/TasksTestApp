package com.example.taskstestapp.presentation.users

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskstestapp.R
import com.example.taskstestapp.domain.users.User

class UserListAdapter(
    private val onItemClick: (User) -> Unit
) : ListAdapter<User, UserListAdapter.UserListItemViewHolder>(UserDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return UserListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        val item = getItem(position)

        Log.d("Adapter", "Got list item: ${item} ")
        holder.tvName.text = item.name
        holder.tvUsername.text = item.username
        holder.tvEmail.text = item.email
        holder.tvCity.text = item.address.city
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    class UserListItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvName: TextView = view.findViewById(R.id.tv_item_name)
        val tvUsername: TextView = view.findViewById(R.id.tv_item_username)
        val tvEmail: TextView = view.findViewById(R.id.tv_item_email)
        val tvCity: TextView = view.findViewById(R.id.tv_item_city)
    }

    private object UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}