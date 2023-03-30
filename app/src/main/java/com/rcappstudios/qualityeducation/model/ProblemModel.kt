package com.rcappstudios.qualityeducation.model

data class ProblemModel(
    val title: String ?= null,
    val imageUrl: String ?= null,
    val description: String ?= null,
    val link: String ?= null,
    val response: HashMap<String, String> ?= null
)