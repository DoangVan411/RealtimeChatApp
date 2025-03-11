package com.example.realtimechatapp.data.repository

import com.example.realtimechatapp.data.source.remote.FirebaseChatDataSource
import com.example.realtimechatapp.domain.model.Chat
import com.example.realtimechatapp.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl(private val firebaseChatDataSource: FirebaseChatDataSource): ChatRepository {
    override suspend fun sendMessage(chat: Chat): String? {
        val chatId = getChatId(chat.senderId, chat.receiverId)
        return firebaseChatDataSource.sendMessage(chatId, chat)
    }

    private fun getChatId(user1: String, user2: String): String {
        return if(user1 < user2) "$user1-$user2" else "$user2-$user1"
    }

    override fun getMessages(chatId: String): Flow<List<Chat>> {
        return firebaseChatDataSource.getMessages(chatId)
    }
}