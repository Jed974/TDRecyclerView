package com.example.tdrecyclerview.network

import com.example.tdrecyclerview.authentication.LoginForm
import com.example.tdrecyclerview.authentication.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm) : Response<LoginResponse>
}