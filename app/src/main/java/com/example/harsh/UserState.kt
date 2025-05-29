package com.example.harsh

sealed class UserState {
    object Idle : UserState()
    object Loading : UserState()
    data class Users(val users: ApiResponse) : UserState()
    data class Error(val error: String) : UserState()
}