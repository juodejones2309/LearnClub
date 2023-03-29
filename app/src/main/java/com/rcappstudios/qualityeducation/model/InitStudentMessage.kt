package com.rcappstudios.qualityeducation.model

data class InitStudentMessage(
    val studentName: String ?= null,
    val studentUserId: String ?= null
)