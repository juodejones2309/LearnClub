package com.zero.chatgpt_androidapp.data.completion.api

import com.zero.chatgpt_androidapp.data.completion.model.image.request.ImageGenerationRequest
import com.zero.chatgpt_androidapp.data.completion.model.image.response.ImageGenerationResponse
import com.zero.chatgpt_androidapp.data.completion.model.response.CompletionResponse
import com.zero.chatgpt_androidapp.data.completion.model.request.CompletionRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CompletionAPIService {

    @POST("completions")
    fun getCompletionResult(@Body completionRequest: CompletionRequest): Call<CompletionResponse>

    @POST("images/generations")
    fun getImageGenerated(@Body imageGenerationRequest: ImageGenerationRequest) : Call<ImageGenerationResponse>
}