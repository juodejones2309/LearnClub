package com.zero.chatgpt_androidapp.networkutils

sealed class ApiResult <out T> {

    data class Success<out T>(val body: T) : ApiResult<T>()

    data class Error(val message: String?) : ApiResult<Nothing>()
}