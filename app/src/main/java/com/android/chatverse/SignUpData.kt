package com.android.chatverse

data class SignUpData(
    val email:String,
    val password:String
)
data class signUpResponse(
    val message: String,
    val userId: String
)
