package com.example.realtimechatapp.domain.usecase

import com.example.realtimechatapp.domain.repository.UserRepository

class GetUserNameUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(uid: String): String {
        return userRepository.getUserName(uid)
    }
}