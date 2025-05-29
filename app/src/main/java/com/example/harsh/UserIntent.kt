package com.example.harsh

sealed class UserIntent {
    object FetchUsers : UserIntent()
}