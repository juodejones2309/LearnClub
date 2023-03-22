package com.rcappstudios.qualityeducation.utils

import java.text.SimpleDateFormat
import java.util.*

fun timeStampToHrs(timeStamp : Long) : String{

    return SimpleDateFormat("h:mma").format(Date(timeStamp ))
}