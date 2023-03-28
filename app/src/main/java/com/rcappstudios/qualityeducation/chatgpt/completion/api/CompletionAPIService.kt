package com.rcappstudios.qualityeducation.chatgpt.completion.api

import com.rcappstudio.placesapi.youtubeDataModel.YoutubeResults
import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.request.ImageGenerationRequest
import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.response.ImageGenerationResponse
import com.rcappstudios.qualityeducation.chatgpt.completion.model.response.CompletionResponse
import com.rcappstudios.qualityeducation.chatgpt.completion.model.request.CompletionRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CompletionAPIService {

    @POST("completions")
    fun getCompletionResult(@Body completionRequest: CompletionRequest): Call<CompletionResponse>

    @POST("images/generations")
    fun getImageGenerated(@Body imageGenerationRequest: ImageGenerationRequest) : Call<ImageGenerationResponse>
    @GET("search")
    fun getYoutubeResults(
        @Query("part") part : String,
        @Query("q") q: String,
        @Query("key") key : String,
        @Query("maxResults") maxResults: Int ?= 20
    ) : Response<YoutubeResults>
}