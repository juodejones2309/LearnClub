package com.rcappstudios.qualityeducation.model

data class Test(val name: String ?= null, val fields: List<Field> ?= null, val creator: String? = null)