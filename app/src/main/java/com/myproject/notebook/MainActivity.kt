package com.myproject.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myproject.notebook.models.NOTE_EXTRA
import com.myproject.notebook.models.Note
import com.myproject.notebook.recycler_view.BtnClickListener
import com.myproject.notebook.recycler_view.NoteAdapter
import com.myproject.notebook.recycler_view.RVModel
import com.myproject.notebook.service.DataGetter
import com.myproject.notebook.service.Utils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Console
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val dg = DataGetter()
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.idRV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val notes = dg.getStrFromJson(applicationContext)?.let { dg.parseData(it) };
        notes?.forEach { note -> Log.i("data", note.toString()+ Utils.convertTimeStart(note.date_start))}

        val datePicker = findViewById<DatePicker>(R.id.datePicker)

        val calendar:Calendar = Calendar.getInstance()
        datePicker.init(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)){

            _, year, pmonth, day ->
            val month = pmonth + 1
            val msg = "SELECTED: $day/$month/$year"
            val date = LocalDate.of(year, month, day)
            val noteList:MutableList<Note> = ArrayList()
            notes?.forEach { note ->
                val dateStart = Utils.convertTimeStart(note.date_start)
                val dateEnd = Utils.convertTimeStart(note.date_end)

                if (date.isEqual(LocalDate.from(dateEnd))){
                    noteList.add(note)
                }

                if (date.isBefore(LocalDate.from(dateEnd)) && (date.isEqual(LocalDate.from(dateStart)) || date.isAfter(LocalDate.from(dateStart))))
                    noteList.add(note)
            }

            val times:Array<RVModel> = generateTimes(noteList, date)
            recyclerView.adapter = NoteAdapter(times, object: BtnClickListener{
                override fun onClick(note: Note) {
                    val intent =Intent(context, ExtraInfoActivity::class.java).apply{
                        putExtra(NOTE_EXTRA, note)
                    }
                    startActivity(intent)
                }
            })
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun generateTimes(notes:MutableList<Note>, date:LocalDate):Array<RVModel>{
        val timesArr = Array(24) { i ->
            RVModel(hour_start = i, hour_end = if (i==23) 0 else i+1)
        }

        for (note:Note in notes){
            val dateStart: LocalDateTime = Utils.convertTimeStart(note.date_start)
            val dateEnd: LocalDateTime = Utils.convertTimeStart(note.date_end)
            var hourStart = 0
            var hourEnd= 23

            if (date.isEqual(LocalDate.from(dateStart)))
                hourStart = dateStart.hour

            if (date.isEqual(LocalDate.from(dateEnd)))
                hourEnd = dateEnd.hour

            for (i in hourStart..hourEnd)
                timesArr[i].note = note
        }

        return timesArr
    }
}