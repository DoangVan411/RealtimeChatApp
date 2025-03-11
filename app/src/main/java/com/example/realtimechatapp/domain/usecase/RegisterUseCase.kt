package com.example.realtimechatapp.domain.usecase

import com.example.realtimechatapp.domain.model.User
import com.example.realtimechatapp.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String, name: String): Boolean {
        return userRepository.register(email, password, name)
    }
}