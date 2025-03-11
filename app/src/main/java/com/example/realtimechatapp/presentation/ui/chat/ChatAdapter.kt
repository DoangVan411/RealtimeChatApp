package com.example.realtimechatapp.presentation.ui.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.realtimechatapp.databinding.ReceiveChatItemBinding
import com.example.realtimechatapp.databinding.SendChatItemBinding
import com.example.realtimechatapp.domain.model.Chat
import com.example.realtimechatapp.presentation.utils.Utils.getTime
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter: ListAdapter<Chat, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
//        Log.d("ChatAdapter", "getItemViewType: ${message.senderId} ${auth.currentUser!!.uid}")
        return if(message.senderId == auth.currentUser!!.uid) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = SendChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ReceiveChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if(holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if(holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    class SentMessageViewHolder(private val binding: SendChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvMessage.text = chat.text
            binding.tvTimestamp.text = getTime(chat.createdAt)
            itemView.setOnClickListener {
                binding.tvTimestamp.visibility = if (binding.tvTimestamp.visibility == android.view.View.VISIBLE) android.view.View.GONE else android.view.View.VISIBLE
            }
        }
    }

    class ReceivedMessageViewHolder(private val binding: ReceiveChatItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.tvMessage.text = chat.text
            binding.tvTimestamp.text = getTime(chat.createdAt)
            itemView.setOnClickListener{
                binding.tvTimestamp.visibility = if (binding.tvTimestamp.visibility == android.view.View.VISIBLE) android.view.View.GONE else android.view.View.VISIBLE
            }
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

}