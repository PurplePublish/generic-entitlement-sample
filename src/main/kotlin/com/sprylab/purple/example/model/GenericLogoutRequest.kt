package com.sprylab.purple.example.model

data class GenericLogoutRequest(
    val appId: String,
    val deviceId: String? = null,
    val accessToken: String
)
