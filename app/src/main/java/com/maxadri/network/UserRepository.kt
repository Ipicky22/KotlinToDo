package com.maxadri.network

import okhttp3.MultipartBody

class UserRepository {

    private val userWebService = Api.userService

    suspend fun getInfo(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateAvatar(avatar: MultipartBody.Part): UserInfo? {
        val response = userWebService.updateAvatar(avatar)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun update(user: UserInfo): UserInfo? {
        val response = userWebService.update(user)
        return if (response.isSuccessful) response.body() else null
    }

}