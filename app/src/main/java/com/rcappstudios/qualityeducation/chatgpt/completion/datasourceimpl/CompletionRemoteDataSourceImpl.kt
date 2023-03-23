package com.rcappstudios.qualityeducation.chatgpt.completion.datasourceimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.rcappstudios.qualityeducation.chatgpt.completion.api.CompletionAPIService
import com.rcappstudios.qualityeducation.chatgpt.completion.datasource.CompletionRemoteDataSource
import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.request.ImageGenerationRequest
import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.response.ImageGenerationResponse
import com.rcappstudios.qualityeducation.chatgpt.completion.model.request.CompletionRequest
import com.rcappstudios.qualityeducation.chatgpt.completion.model.response.CompletionResponse
import com.rcappstudios.qualityeducation.chatgpt.networkutils.ApiResult
import com.rcappstudios.qualityeducation.chatgpt.networkutils.safeApiCall
import kotlinx.coroutines.Dispatchers

class CompletionRemoteDataSourceImpl(
    private val completionAPIService: CompletionAPIService
) : CompletionRemoteDataSource {

    override fun getCompletionResult(completionRequest: CompletionRequest): LiveData<CompletionResponse> =
        liveData {
            val result = safeApiCall(Dispatchers.IO) {
                completionAPIService.getCompletionResult(completionRequest)
            }

            when (result) {
                is ApiResult.Success -> {
                    emit(result.body)
                }
                is ApiResult.Error -> {
                    val error = CompletionResponse()
                    Log.d("TAGData", "getData: ${result.message}")
                    emit(error)
                }
                else -> emit(CompletionResponse())
            }

        }

    override fun getImageGenerationResult(imageGenerationRequest: ImageGenerationRequest): LiveData<ImageGenerationResponse> =
        liveData {
            val result = safeApiCall(Dispatchers.IO) {
                completionAPIService.getImageGenerated(imageGenerationRequest)
            }

            when (result) {
                is ApiResult.Success -> {
                    emit(result.body)
                }
                is ApiResult.Error -> {
                    val error = ImageGenerationResponse()
                    Log.d("TAGData", "getData: ${result.message}")
                    emit(error)
                }
                else -> emit(ImageGenerationResponse())
            }
        }


}