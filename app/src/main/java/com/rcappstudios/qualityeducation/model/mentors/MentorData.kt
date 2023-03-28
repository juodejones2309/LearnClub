package com.rcappstudios.qualityeducation.model.mentors

data class MentorData(
    val mentorName: String ?= null,
    val mentorNumber: String ?= null,
    val subject: String ?= null,
    val userId: String ?= null
)