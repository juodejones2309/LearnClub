package com.zero.chatgpt_androidapp.data.completion.model.response


import com.google.gson.annotations.SerializedName

data class Usage(
    @SerializedName("completion_tokens")
    val completionTokens: Int,
    @SerializedName("prompt_tokens")
    val promptTokens: Int,
    @SerializedName("total_tokens")
    val totalTokens: Int
)