package com.myproject.notebook.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val NOTE_EXTRA = "NOTE_EXTRA"

var noteList = mutableListOf<Note>()

data class Note (
    var id: Int,
    var date_start:Long,
    var date_end:Long,
    var name: String,
    var description: String) :Serializable{}