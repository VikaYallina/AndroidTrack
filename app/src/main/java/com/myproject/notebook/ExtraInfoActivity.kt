package com.myproject.notebook

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.myproject.notebook.models.NOTE_EXTRA
import com.myproject.notebook.models.Note
import com.myproject.notebook.service.Utils
import io.realm.Realm
import io.realm.kotlin.where
import java.time.format.DateTimeFormatter

class ExtraInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val realm: Realm = Realm.getDefaultInstance()
        var noteId: String? = ""

        // Get note id data from parent action
        if (savedInstanceState == null){
            val extras = intent.extras
            if (extras == null)
                noteId=""
            else noteId = extras.getString(NOTE_EXTRA)
        }else{
            noteId = savedInstanceState.getString(NOTE_EXTRA)
        }
        var note: Note? = null

        // Find a note with received id
        realm.executeTransaction{ realm_ ->
            note = realm_.where<Note>().equalTo("id", noteId).findFirst()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_info)

        setInfo(note)
    }

    private fun setInfo(note:Note?){
        if (note!=null){
            val dateStart = Utils.convertMillisToTime(note.date_start)
            val dateEnd = Utils.convertMillisToTime(note.date_end)

            "Note name: ${note.name}".also { findViewById<TextView>(R.id.extra_title).text = it }
            "Date start: ${Utils.formatDate(dateStart)}".also { findViewById<TextView>(R.id.extra_start).text = it }
            "Date end: ${Utils.formatDate(dateEnd)}".also { findViewById<TextView>(R.id.extra_end).text = it }
            "Description: ${note.description}".also { findViewById<TextView>(R.id.extra_desc).text = it }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}