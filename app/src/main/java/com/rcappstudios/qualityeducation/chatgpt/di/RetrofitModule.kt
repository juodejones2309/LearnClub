package com.rcappstudios.qualityeducation.chatgpt.di

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rcappstudios.qualityeducation.chatgpt.completion.api.CompletionAPIService
import com.rcappstudios.qualityeducation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request =
                chain.request().newBuilder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer ${Constants.CHAT_GPT_TOKEN}")
                .build()
            chain.proceed(newRequest)
        }).build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit{
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun createCompletionAPIService(retrofit: Retrofit): CompletionAPIService{
        return retrofit.create(CompletionAPIService:: class.java)
    }
}