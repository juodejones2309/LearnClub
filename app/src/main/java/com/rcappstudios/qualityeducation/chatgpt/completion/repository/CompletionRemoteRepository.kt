package com.zero.chatgpt_androidapp.data.completion.repository

import com.zero.chatgpt_androidapp.data.completion.datasource.CompletionRemoteDataSource

class CompletionRemoteRepository (private val completionRemoteDataSource: CompletionRemoteDataSource):
    CompletionRemoteDataSource by completionRemoteDataSource