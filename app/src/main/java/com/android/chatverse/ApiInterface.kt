package com.android.chatverse

import com.android.chatverse.DataClasses.UserProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiInterface {
    @POST("login")
    fun login(@Body loginRequest:LoginRequest): Call<LoginResponse>


    @POST("profile")
    fun signup(@Body signupRequest: SignUpData):Call<signUpResponse>

    @Multipart
    @PATCH("update/{userId}")
    fun updateProfile(
        @Part("name") name: RequestBody,
        @Part profilePic: MultipartBody.Part,
        @Part("age") age: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("lookingForGender") lookingFor: RequestBody,
        @Path("userId") userId: String
    ): Call<LoginResponse>

//    @GET("getUser/v2n1TDyMv")
//    fun getUserProfile(userId:UserId)
    @GET("getUser/{userId}")
    fun getUserProfile(@Path("userId") userId: String): Call<UserProfile>

}