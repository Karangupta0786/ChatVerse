package com.android.chatverse

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object retrofitObject {
    val retroit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://chatverse-phi.vercel.app")
        .build()
        .create(ApiInterface::class.java)
}