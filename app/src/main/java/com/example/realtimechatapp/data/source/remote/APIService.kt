package com.example.realtimechatapp.data.source.remote

import com.example.realtimechatapp.data.dto.FCMRequest
import com.example.realtimechatapp.data.dto.FCMResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type: application/json"
    )
    @POST("/v1/projects/realtimechatapp-8ac95/messages:send")
    suspend fun pushNotification(
        @Body request: FCMRequest,
        @Header("Authorization") accessToken: String
    ): Response<FCMResponse>
}