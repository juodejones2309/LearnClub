package com.zero.chatgpt_androidapp.data.completion.datasource

import androidx.lifecycle.LiveData
import com.zero.chatgpt_androidapp.data.completion.model.image.request.ImageGenerationRequest
import com.zero.chatgpt_androidapp.data.completion.model.image.response.ImageGenerationResponse
import com.zero.chatgpt_androidapp.data.completion.model.response.CompletionResponse
import com.zero.chatgpt_androidapp.data.completion.model.request.CompletionRequest
import kotlinx.coroutines.flow.Flow

interface CompletionRemoteDataSource {

    fun getCompletionResult(completionRequest: CompletionRequest) : LiveData<CompletionResponse>

    fun getImageGenerationResult(imageGenerationRequest: ImageGenerationRequest): LiveData<ImageGenerationResponse>
}