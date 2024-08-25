package com.example.collegefixit.networking

import retrofit2.http.Body
import retrofit2.http.POST

interface MyNetworkService {
    @POST("submitToken")
    suspend fun register(@Body tokenRequest: TokenRequest): TokenResponse
}
