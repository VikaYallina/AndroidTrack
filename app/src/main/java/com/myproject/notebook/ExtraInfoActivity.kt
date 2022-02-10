package com.myproject.notebook

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.myproject.notebook.models.NOTE_EXTRA
import com.myproject.notebook.models.Note
import com.myproject.notebook.service.Utils
import java.time.format.DateTimeFormatter

class ExtraInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var note: Note?
        if (savedInstanceState == null){
            val extras = intent.extras
            if (extras == null)
                note=null
            else note = extras.getSerializable(NOTE_EXTRA) as Note?
        }else{
            note = savedInstanceState.getSerializable(NOTE_EXTRA) as Note?
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_info)

        setInfo(note);
    }

    private fun setInfo(note:Note?){
        if (note!=null){
            val dateStart = Utils.convertTimeStart(note.date_start)
            val dateEnd = Utils.convertTimeStart(note.date_end)
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

            "Note name: ${note.name}".also { findViewById<TextView>(R.id.extra_title).text = it }
            "Date start: ${formatter.format(dateStart)}".also { findViewById<TextView>(R.id.extra_start).text = it }
            "Date end: ${formatter.format(dateEnd)}".also { findViewById<TextView>(R.id.extra_end).text = it }
            "Description: ${note.description}".also { findViewById<TextView>(R.id.extra_desc).text = it }
        }
    }
}