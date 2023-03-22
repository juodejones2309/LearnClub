package com.rcappstudios.qualityeducation.chatgpt.completion.model.response


import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("finish_reason")
    val finishReason: String,
    @SerializedName("index")
    val index: Int,
    @SerializedName("logprobs")
    val logprobs: Any,
    @SerializedName("text")
    val text: String
)