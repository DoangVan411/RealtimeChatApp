package com.example.realtimechatapp.presentation.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.realtimechatapp.RealtimeChatAppApplication
import com.example.realtimechatapp.domain.model.User
import com.example.realtimechatapp.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserListViewModel(private val getUsersUseCase: GetUsersUseCase): ViewModel() {
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList.asStateFlow()

    fun getUsers() {
        viewModelScope.launch {
            getUsersUseCase().collect { userList ->
                _userList.value = userList
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as RealtimeChatAppApplication
                return UserListViewModel(
                    getUsersUseCase = GetUsersUseCase(application.repositoryModule.userRepository)
                ) as T
            }
        }
    }
}
