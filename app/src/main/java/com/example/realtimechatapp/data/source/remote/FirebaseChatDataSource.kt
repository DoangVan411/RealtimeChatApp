package com.example.realtimechatapp.data.source.remote

import android.util.Log
import com.example.realtimechatapp.domain.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseChatDataSource(
    private val firestore: FirebaseFirestore
) {

    suspend fun sendMessage(chatId: String, chat: Chat): String? {
        firestore.collection("chats").document(chatId).collection("messages")
            .add(chat).await()

        val snapshot = firestore.collection("users")
            .document(chat.receiverId)
            .get()
            .await()

        val fcmToken = snapshot.getString("fcmToken")
        Log.d("SEND MESSAGE", "$fcmToken")
        return fcmToken
    }

    fun getMessages(chatId: String): Flow<List<Chat>> = callbackFlow{
        val listener = firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("createdAt")
            .addSnapshotListener {snapshot, _ ->
                val messages = snapshot?.toObjects(Chat::class.java) ?: emptyList()
                trySend(messages)
            }
        awaitClose {
            listener.remove()
        }
    }
}