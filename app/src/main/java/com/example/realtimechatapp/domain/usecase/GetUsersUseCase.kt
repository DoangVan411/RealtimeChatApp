package com.example.realtimechatapp.domain.usecase

import com.example.realtimechatapp.domain.model.User
import com.example.realtimechatapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<List<User>> {
        return userRepository.getUsers()
    }
}