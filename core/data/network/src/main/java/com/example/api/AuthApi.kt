package com.example.api

import com.example.api.dto.LoginRequest
import com.example.api.dto.LoginResponse
import com.example.api.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest)
}