package com.example.realtimechatapp.data.repository

import com.example.realtimechatapp.data.source.remote.APIService
import com.example.realtimechatapp.data.dto.FCMRequest
import com.example.realtimechatapp.data.dto.FCMResponse
import com.example.realtimechatapp.domain.repository.NotificationRepository
import retrofit2.Response

class NotificationRepositoryImpl(private val apiService: APIService): NotificationRepository {
    override suspend fun pushNotification(
        request: FCMRequest,
        accessToken: String
    ): Response<FCMResponse> {
        return try {
            val response = apiService.pushNotification(request, "Bearer $accessToken")
//            if(response.isSuccessful) {
//                Log.d("NOTIFICATION", "Gửi thông báo thành công: ${response.body()?.name}")
//            } else {
//                Log.e("FCM", "Lỗi gửi thông báo: ${response.errorBody()?.string()}")
//            }
            response
        } catch (e: Exception) {
//            Log.d("Test", e.message.toString())
            Response.error(500, okhttp3.ResponseBody.create(null, "Exception: ${e.message}"))
        }
    }

}