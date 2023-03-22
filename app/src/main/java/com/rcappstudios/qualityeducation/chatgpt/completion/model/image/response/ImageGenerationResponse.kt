package com.rcappstudios.qualityeducation.chatgpt.completion.model.image.response


import com.google.gson.annotations.SerializedName

data class ImageGenerationResponse(
    @SerializedName("created")
    val created: Int ?= null,
    @SerializedName("data")
    val `data`: List<Data> ?= null
)