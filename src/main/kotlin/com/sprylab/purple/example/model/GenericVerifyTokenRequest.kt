package com.sprylab.purple.example.model


data class GenericVerifyTokenRequest(
    val appId: String,
    val deviceId: String? = null,
    val accessToken: String
)
