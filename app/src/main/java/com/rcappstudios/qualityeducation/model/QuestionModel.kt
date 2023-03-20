package com.rcappstudios.qualityeducation.model

import android.media.Image

data class QuestionModel(
    val userImageUrl: String ?= null,
    val userName: String ?= null,
    val question: String ?= null,
    val imageUrl: String?= null,
    val timeStamp: Long?= null,
    val subjectName: String?= null,
    val questionID: String?= null
)