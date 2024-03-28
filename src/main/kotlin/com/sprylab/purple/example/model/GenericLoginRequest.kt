package com.sprylab.purple.example.model

data class GenericLoginRequest(
    val appId: String,
    val deviceId: String? = null,
    val username: String,
    val password: String? = null
)
