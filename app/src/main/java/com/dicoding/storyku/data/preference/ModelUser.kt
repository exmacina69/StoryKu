package com.dicoding.storyku.data.preference

data class  ModelUser(
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)