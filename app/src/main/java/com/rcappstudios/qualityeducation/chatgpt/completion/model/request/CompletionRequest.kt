package com.rcappstudios.qualityeducation.chatgpt.completion.model.request


import com.google.gson.annotations.SerializedName

data class CompletionRequest(
    @SerializedName("max_tokens")
    val maxTokens: Int,
    @SerializedName("model")
    val model: String,
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("stream")
    val stream: Boolean,
    @SerializedName("temperature")
    val temperature: Int
)