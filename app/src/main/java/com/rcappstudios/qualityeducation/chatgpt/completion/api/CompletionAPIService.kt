package com.rcappstudios.qualityeducation.chatgpt.completion.api

import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.request.ImageGenerationRequest
import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.response.ImageGenerationResponse
import com.rcappstudios.qualityeducation.chatgpt.completion.model.response.CompletionResponse
import com.rcappstudios.qualityeducation.chatgpt.completion.model.request.CompletionRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CompletionAPIService {

    @POST("completions")
    fun getCompletionResult(@Body completionRequest: CompletionRequest): Call<CompletionResponse>

    @POST("images/generations")
    fun getImageGenerated(@Body imageGenerationRequest: ImageGenerationRequest) : Call<ImageGenerationResponse>
}