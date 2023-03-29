package com.rcappstudios.qualityeducation.model

data class StudentData(
    val name: String ?= null,
    val grade: String ?= null,
    val phoneNumber: String ?= null,
    val imageUrl: String? = null,
    var testAttended: Int? = 0,
    var score: Int?= 0
)