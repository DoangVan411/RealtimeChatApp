package com.example.realtimechatapp.presentation.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.realtimechatapp.domain.model.User
import com.example.realtimechatapp.databinding.ItemUserBinding

class UserListAdapter(private val onItemClick: (User) -> Unit): ListAdapter<User, UserListAdapter.UserViewHolder>(
    UserDiffCallback()
) {
    inner class UserViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvUserName.text = user.name
                tvUserEmail.text = user.email
                binding.root.setOnClickListener {onItemClick(user)}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserDiffCallback: DiffUtil.ItemCallback<User> () {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}