package com.example.harsh

class UserRepository(private val apiService: ApiService) {
    suspend fun fetchUsers(): ApiResponse {
        return apiService.getUsers()
    }
}
