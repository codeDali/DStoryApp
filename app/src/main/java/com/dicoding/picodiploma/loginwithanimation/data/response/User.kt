package com.dicoding.picodiploma.loginwithanimation.data.response

data class User(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)