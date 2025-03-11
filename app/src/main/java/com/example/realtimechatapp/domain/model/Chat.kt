package com.example.realtimechatapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Chat(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    @ServerTimestamp
    val createdAt: Date = Date()
)
