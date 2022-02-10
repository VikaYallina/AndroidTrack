package com.myproject.notebook.service

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Utils {
    companion object{
        fun convertTimeStart(timeInMilis:Long): LocalDateTime {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            val instant = Instant.ofEpochMilli(timeInMilis)
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            return date
        }
    }
}