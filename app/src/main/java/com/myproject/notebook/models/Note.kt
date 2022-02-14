package com.myproject.notebook.models

import android.os.Build
import androidx.annotation.RequiresApi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.io.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val NOTE_EXTRA = "NOTE_EXTRA"

var noteList = mutableListOf<Note>()

open class Note (
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    var date_start:Long = 0L,
    var date_end:Long = 0L,
    var name: String = "",
    var description: String = ""
        ) : RealmObject() {
    }