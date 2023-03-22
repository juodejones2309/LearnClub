package com.zero.chatgpt_androidapp.networkutils

import android.util.Log
import com.rcappstudios.qualityeducation.chatgpt.networkutils.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.io.IOException


const val RESPONSE_STATUS = "success"
const val RESPONSE_ERROR = "error"
const val RESPONSE_DATA = "data"

const val DEFAULT_CONNECT_TIMEOUT_SECONDS = 60L
const val DEFAULT_READ_TIMEOUT_SECONDS = 60L
const val DEFAULT_WRITE_TIMEOUT_SECONDS = 10L

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> Call<T>
) : ApiResult<T> {
    return withContext(dispatcher) {
        try {
            Log.d("NETWORK", "Calling")
            val requestTime = System.currentTimeMillis()
            Log.d("NETWORK","===http REQUEST TIME=== $requestTime")
            val response = apiCall().execute()
            val responseTimeBeforeParse = System.currentTimeMillis()
            Log.d("NETWORK","RESPONSE TIME B4 PARSE " + (responseTimeBeforeParse - requestTime))
            val responseBody = response.body()
            Log.d("TAG", "safeApiCall: ${response.code()} ${responseBody.toString()}")
            if (responseBody != null){
                val responseTimeAfterParse = System.currentTimeMillis()
                Log.d("NETWORK","==PARSE TIME== " + (responseTimeAfterParse - responseTimeBeforeParse))
                Log.d("NETWORK","==http RESPONSE TIME== " + (responseTimeAfterParse - requestTime))
                Log.d("NETWORK", "Calling")
                ApiResult.Success(body = responseBody)
            }else{
                ApiResult.Error(message = response.message())
            }
        }catch (e: IOException){
            Log.d("NETWORK", "Exception : "+e.message)
            ApiResult.Error(message = e.message)
        }catch (e: Exception){
            Log.d("NETWORK", "Exception : "+e.message)
            ApiResult.Error(message = e.message)
        }
    }
}