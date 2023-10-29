package com.android.chatverse

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    val retroit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://chatverse-phi.vercel.app")
        .build()
        .create(ApiInterface::class.java)

    val retroit2 = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://chatverse-phi.vercel.app")
        .build()
        .create(ApiService::class.java)

}