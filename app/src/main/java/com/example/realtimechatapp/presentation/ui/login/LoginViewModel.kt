package com.example.realtimechatapp.presentation.ui.login

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.realtimechatapp.RealtimeChatAppApplication
import com.example.realtimechatapp.domain.usecase.GetMessagesUseCase
import com.example.realtimechatapp.domain.usecase.GetUserNameUseCase
import com.example.realtimechatapp.domain.usecase.LoginUseCase
import com.example.realtimechatapp.domain.usecase.PushNotificationUseCase
import com.example.realtimechatapp.domain.usecase.SendMessageUseCase
import com.example.realtimechatapp.presentation.ui.chat.ChatViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase): ViewModel() {
    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> get() = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = loginUseCase(email, password)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as RealtimeChatAppApplication
                return LoginViewModel(
                    loginUseCase = LoginUseCase(application.repositoryModule.userRepository)
                ) as T
            }
        }
    }
}