package com.android.chatverse

data class LoginRequest(
    val email:String,
    val password:String
)

data class LoginResponse(
    val message: String,
    val mesiboDetails: MesiboDetails,
    val userId:String
)

data class MesiboDetails(
    val uid: Long,
    val token: String,
    val address: String
)
