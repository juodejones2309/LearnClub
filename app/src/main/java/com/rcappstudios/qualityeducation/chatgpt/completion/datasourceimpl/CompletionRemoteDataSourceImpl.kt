package com.zero.chatgpt_androidapp.data.completion.datasourceimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.zero.chatgpt_androidapp.networkutils.ApiResult
import com.zero.chatgpt_androidapp.data.completion.api.CompletionAPIService
import com.zero.chatgpt_androidapp.data.completion.datasource.CompletionRemoteDataSource
import com.zero.chatgpt_androidapp.data.completion.model.image.request.ImageGenerationRequest
import com.zero.chatgpt_androidapp.data.completion.model.image.response.ImageGenerationResponse
import com.zero.chatgpt_androidapp.data.completion.model.response.CompletionResponse
import com.zero.chatgpt_androidapp.data.completion.model.request.CompletionRequest
import com.zero.chatgpt_androidapp.networkutils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CompletionRemoteDataSourceImpl(
    private val completionAPIService: CompletionAPIService
) : CompletionRemoteDataSource {

    override fun getCompletionResult(completionRequest: CompletionRequest): LiveData<CompletionResponse> = liveData{
        val result = safeApiCall(Dispatchers.IO){
            completionAPIService.getCompletionResult(completionRequest)
        }

        when(result){
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

    override fun getImageGenerationResult(imageGenerationRequest: ImageGenerationRequest): LiveData<ImageGenerationResponse> = liveData{
        val result = safeApiCall(Dispatchers.IO){
            completionAPIService.getImageGenerated(imageGenerationRequest)
        }

        when(result){
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