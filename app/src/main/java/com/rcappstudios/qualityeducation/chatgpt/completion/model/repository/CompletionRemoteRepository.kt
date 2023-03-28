package com.rcappstudios.qualityeducation.chatgpt.completion.model.repository

import com.rcappstudios.qualityeducation.chatgpt.completion.datasource.CompletionRemoteDataSource

class CompletionRemoteRepository (private val completionRemoteDataSource: CompletionRemoteDataSource):
    CompletionRemoteDataSource by completionRemoteDataSource