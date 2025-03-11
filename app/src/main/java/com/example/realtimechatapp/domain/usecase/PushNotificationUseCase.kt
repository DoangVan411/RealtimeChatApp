package com.example.realtimechatapp.domain.usecase

import com.example.realtimechatapp.data.dto.FCMRequest
import com.example.realtimechatapp.data.dto.FCMResponse
import com.example.realtimechatapp.domain.repository.NotificationRepository
import retrofit2.Response

class PushNotificationUseCase(private val notificationRepository: NotificationRepository) {
    suspend operator fun invoke(request: FCMRequest, accessToken: String): Response<FCMResponse> {
        return notificationRepository.pushNotification(request, accessToken)
    }
}