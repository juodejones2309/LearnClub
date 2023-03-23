package com.rcappstudios.qualityeducation.model

data class Field(val label: String ?= null, val dataType: String ?= null, val items: List<String>? = null)