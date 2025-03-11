package com.example.realtimechatapp.presentation

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

suspend fun getFcmToken(): String {
    return try {
        FirebaseMessaging.getInstance().token.await()
    } catch (e: Exception) {
        ""
    }
}