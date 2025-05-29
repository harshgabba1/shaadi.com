package com.example.harsh

import retrofit2.http.GET

interface ApiService {
    @GET("api/?results=10")
    suspend fun getUsers(): ApiResponse
}