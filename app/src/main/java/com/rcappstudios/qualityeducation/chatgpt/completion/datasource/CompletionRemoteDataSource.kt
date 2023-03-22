package com.rcappstudios.qualityeducation.chatgpt.completion.datasource

import androidx.lifecycle.LiveData
import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.request.ImageGenerationRequest
import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.response.ImageGenerationResponse
import com.rcappstudios.qualityeducation.chatgpt.completion.model.response.CompletionResponse
import com.rcappstudios.qualityeducation.chatgpt.completion.model.request.CompletionRequest

interface CompletionRemoteDataSource {

    fun getCompletionResult(completionRequest: CompletionRequest) : LiveData<CompletionResponse>

    fun getImageGenerationResult(imageGenerationRequest: ImageGenerationRequest): LiveData<ImageGenerationResponse>
}