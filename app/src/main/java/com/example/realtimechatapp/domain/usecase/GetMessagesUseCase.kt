package com.example.realtimechatapp.domain.usecase

import com.example.realtimechatapp.domain.model.Chat
import com.example.realtimechatapp.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(private val chatRepository: ChatRepository) {
    operator fun invoke(chatId: String): Flow<List<Chat>> {
        return chatRepository.getMessages(chatId)
    }
}