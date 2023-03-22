package com.rcappstudios.qualityeducation.chatgpt.di

import com.zero.chatgpt_androidapp.data.completion.api.CompletionAPIService
import com.zero.chatgpt_androidapp.data.completion.datasource.CompletionRemoteDataSource
import com.zero.chatgpt_androidapp.data.completion.datasourceimpl.CompletionRemoteDataSourceImpl
import com.zero.chatgpt_androidapp.data.completion.repository.CompletionRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CompletionModule {

    @Singleton
    @Provides
    fun providesCompletionRemoteDataSource(completionAPIService: CompletionAPIService): CompletionRemoteDataSource {
        return CompletionRemoteDataSourceImpl(completionAPIService)
    }


    @Singleton
    @Provides
    fun providesCompletionRemoteRepository(completionRemoteDataSource: CompletionRemoteDataSource): CompletionRemoteRepository{
        return CompletionRemoteRepository(completionRemoteDataSource)
    }

}