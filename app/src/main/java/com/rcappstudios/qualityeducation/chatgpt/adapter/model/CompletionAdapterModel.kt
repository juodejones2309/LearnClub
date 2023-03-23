package com.rcappstudios.qualityeducation.chatgpt.adapter.model

data class CompletionAdapterModel(
    val text: String ?= null,
    val isBot: Boolean = false
)