package com.rcappstudios.qualityeducation.chatgpt.completion.model.response


import com.google.gson.annotations.SerializedName

data class CompletionResponse(
    @SerializedName("choices")
    val choices: List<Choice> ?= null,
    @SerializedName("created")
    val created: Int?= null,
    @SerializedName("id")
    val id: String?= null,
    @SerializedName("model")
    val model: String?= null,
    @SerializedName("object")
    val objectX: String?= null,
    @SerializedName("usage")
    val usage: Usage?= null
)