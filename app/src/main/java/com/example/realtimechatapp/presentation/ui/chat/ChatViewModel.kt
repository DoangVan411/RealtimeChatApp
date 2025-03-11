package com.example.realtimechatapp.presentation.ui.chat

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.realtimechatapp.RealtimeChatAppApplication
import com.example.realtimechatapp.data.source.remote.AccessToken
import com.example.realtimechatapp.domain.model.Chat
import com.example.realtimechatapp.data.dto.FCMRequest
import com.example.realtimechatapp.data.dto.Message
import com.example.realtimechatapp.data.dto.Notification
import com.example.realtimechatapp.domain.usecase.GetMessagesUseCase
import com.example.realtimechatapp.domain.usecase.GetUserNameUseCase
import com.example.realtimechatapp.domain.usecase.PushNotificationUseCase
import com.example.realtimechatapp.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val pushNotification: PushNotificationUseCase
): ViewModel() {

    private val _messages = MutableStateFlow<List<Chat>>(emptyList())
    val messages: StateFlow<List<Chat>> = _messages.asStateFlow()

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val pendingMessages = mutableListOf<Chat>()

    fun sendMessage(chat: Chat, uid: String, context: Context) {
        viewModelScope.launch {
            val fcmToken = sendMessageUseCase(chat) {
                addPendingMessage(chat)
            }
            if (fcmToken != null) {
                sendNoti(fcmToken, getSenderName(uid), chat.text, context)
            } else {
                Log.e("Error", "Failed to get FCM Token")
            }
        }
    }

    private fun addPendingMessage(chat: Chat) {
        pendingMessages.add(chat)
    }

    fun onNetworkAvailable(context: Context) {
        pendingMessages.forEach { sendMessage(it, it.senderId, context) }
        pendingMessages.clear()
    }

    private suspend fun getSenderName(uid: String): String {
        return getUserNameUseCase(uid)
    }

    fun getUserName(uid: String) {
        viewModelScope.launch{
            _userName.value = getUserNameUseCase(uid)
        }
    }

    fun getChatId(user1: String, user2: String): String {
        return if(user1 < user2) "$user1-$user2" else "$user2-$user1"
    }

    fun getMessages(chatId: String) {
        viewModelScope.launch {
            getMessagesUseCase(chatId).collectLatest { messageList ->
                _messages.value = messageList
            }
        }
    }

    private fun sendNoti(token: String, title: String, body: String, context: Context){
        val request = FCMRequest(
            message = Message(
                token = token,
                notification = Notification(
                    title = title,
                    body = body
                )
            )
        )

        viewModelScope.launch{
            val accessToken = AccessToken.getAccessToken(context)
            pushNotification(request, accessToken!!)
            Log.d("Test", "token: $accessToken")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as RealtimeChatAppApplication
                return ChatViewModel(
                    getMessagesUseCase = GetMessagesUseCase(application.repositoryModule.chatRepository),
                    getUserNameUseCase = GetUserNameUseCase(application.repositoryModule.userRepository),
                    sendMessageUseCase = SendMessageUseCase(application.repositoryModule.chatRepository, application.repositoryModule.networkRepository),
                    pushNotification = PushNotificationUseCase(application.repositoryModule.notificationRepository)
                ) as T
            }
        }
    }
}