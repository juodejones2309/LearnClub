package com.rcappstudios.qualityeducation.model

data class ProblemStatement(
    val title: String ?= null,
    val imageUrl: String ?= null,
    val link: String ?= null,
    val description: String ?= null
)