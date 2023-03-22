package com.zero.chatgpt_androidapp.data.completion.model.image.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("url")
    val url: String
)