package com.rcappstudios.qualityeducation.model

import javax.security.auth.Subject

data class RoomModel(
    val hostID: String ?= null,
    val topicName: String ?= null,
    val timeStamp: Long ?= null,
    val creatorName: String ?= null,
    val roomID: String ?= null,
    val subject: String ?= null
)