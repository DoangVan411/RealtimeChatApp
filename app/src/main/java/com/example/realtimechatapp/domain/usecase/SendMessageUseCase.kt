package com.example.realtimechatapp.domain.usecase

import com.example.realtimechatapp.domain.model.Chat
import com.example.realtimechatapp.domain.repository.ChatRepository
import com.example.realtimechatapp.domain.repository.NetworkRepository

class SendMessageUseCase(
    private val chatRepository: ChatRepository,
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(chat: Chat, onPending: () -> Unit): String? {
        if(networkRepository.isNetworkAvailable()) {
            return chatRepository.sendMessage(chat)
        }else {
            onPending()
            return null
        }
    }
}