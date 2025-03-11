package com.example.realtimechatapp.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.realtimechatapp.RealtimeChatAppApplication
import com.example.realtimechatapp.domain.model.User
import com.example.realtimechatapp.domain.usecase.RegisterUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUseCase: RegisterUseCase): ViewModel() {

    private val _registerState = MutableLiveData<Boolean>()
    val registerState: LiveData<Boolean> = _registerState


    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            val success = registerUseCase(email, password, name)
            _registerState.value = success
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as RealtimeChatAppApplication
                return RegisterViewModel(
                    registerUseCase = RegisterUseCase(application.repositoryModule.userRepository)
                ) as T
            }

        }
    }
}
