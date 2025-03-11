package com.example.realtimechatapp.domain.usecase

import com.example.realtimechatapp.domain.model.Chat
import com.example.realtimechatapp.domain.repository.ChatRepository

class ResendMessagesUseCase(private val chatRepository: ChatRepository) {
    private val pendingMessages = mutableListOf<Chat>()
    fun addMessage(chat: Chat) {
        pendingMessages.add(chat)
    }

    suspend fun invoke() {
        pendingMessages.forEach { message ->
            chatRepository.sendMessage(message)
        }
        pendingMessages.clear()
    }
}