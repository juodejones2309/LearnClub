package com.rcappstudio.indoorfarming.api

import com.rcappstudios.qualityeducation.retrofit.api.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val youtubeUrl = "https://youtube.googleapis.com/youtube/v3/"


    val youtubeApi: Api by lazy {
        Retrofit.Builder()
            .baseUrl(youtubeUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

}