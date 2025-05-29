package com.example.harsh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _state = MutableStateFlow<UserState>(UserState.Idle)
    val state: StateFlow<UserState> = _state.asStateFlow()

    fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.FetchUsers -> fetchUsers()
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _state.value = UserState.Loading
            try {
                val users = repository.fetchUsers()
                _state.value = UserState.Users(users)
            } catch (e: Exception) {
                _state.value = UserState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
