package com.rcappstudios.qualityeducation.model

class MessageModel (
    var content  :String ?= null,
    var messagerId: String ?= null,
    var messagerName: String ?= null,
    var timestamp  :Long ?= 0
)