package com.myproject.notebook.service

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Utils {
    companion object{
        fun convertMillisToTime(timeInMilis:Long): LocalDateTime {
            val instant = Instant.ofEpochMilli(timeInMilis)
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            return date
        }

        fun convertTimeToMillis(time:LocalDateTime):Long{
            return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        fun formatDate(time:LocalDateTime):String{
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            return formatter.format(time)
        }
    }

}