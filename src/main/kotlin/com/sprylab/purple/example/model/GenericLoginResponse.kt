package com.sprylab.purple.example.model

data class GenericLoginResponse(
    val token: String,
    val userId: String? = null
)
