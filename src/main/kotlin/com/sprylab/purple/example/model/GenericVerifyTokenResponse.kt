package com.sprylab.purple.example.model

data class GenericVerifyTokenResponse(
    val accessToken: String,
    val userId: String? = null
)
