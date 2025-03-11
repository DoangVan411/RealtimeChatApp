package com.example.realtimechatapp.domain.repository

import com.example.realtimechatapp.data.dto.FCMRequest
import com.example.realtimechatapp.data.dto.FCMResponse
import retrofit2.Response

interface NotificationRepository {
    suspend fun pushNotification(request: FCMRequest, accessToken: String): Response<FCMResponse>
}