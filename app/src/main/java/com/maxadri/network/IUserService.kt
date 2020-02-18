package com.maxadri.network

import retrofit2.Response
import retrofit2.http.GET

interface IUserService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>
}
