package com.myproject.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myproject.notebook.models.NOTE_EXTRA
import com.myproject.notebook.models.Note
import com.myproject.notebook.recycler_view.BtnClickListener
import com.myproject.notebook.recycler_view.NoteAdapter
import com.myproject.notebook.recycler_view.RVModel
import com.myproject.notebook.service.DataGetter
import com.myproject.notebook.service.Utils
import io.realm.Realm
import io.realm.kotlin.where
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    val context = this
    private lateinit var realm:Realm
    private lateinit var recyclerView:RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        realm = Realm.getDefaultInstance()

        recyclerView= findViewById(R.id.idRV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        realm.executeTransactionAsync { realm_ -> realm_.createAllFromJson(Note::class.java,DataGetter.getStrFromJson(context)) }


        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        findViewById<FloatingActionButton>(R.id.floating_action_button)
            .setOnClickListener {onFabBtnClick()}

        val calendar:Calendar = Calendar.getInstance()
        datePicker.init(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)){

            _, year, pmonth, day ->
            onChooseDate(year, pmonth, day)
        }
    }

    fun onChooseDate(year:Int, pmonth:Int, day:Int){
        val month = pmonth + 1
        val date = LocalDate.of(year, month, day)
        var noteList:MutableList<Note> = ArrayList()

        val dateStart = LocalDateTime.of(year, month, day, 0,0, 0)
        val dateEnd = LocalDateTime.of(year, month, day, 23,59,59)

        // 00:00:00 in millis
        val dateStartMilli = Utils.convertTimeToMillis(dateStart)
        // 23:59:59 in millis
        val dateEndMilli = Utils.convertTimeToMillis(dateEnd)

        // From db select notes in which:
        //          1. note's end date is somewhere during the picked date
        //
        //          2. note's end date is after the picked date
        //          AND note's start date is before the end of picked date

        realm.executeTransaction { realm_ -> noteList = realm_
            .where<Note>()
            .between("date_end",dateStartMilli,dateEndMilli)
            .or()
            .beginGroup()
            .greaterThanOrEqualTo("date_end",dateEndMilli)
            .and()
            .lessThanOrEqualTo("date_start",dateEndMilli)
            .endGroup()
            .findAll()}

        //generate recycler view for each hour of the day and set onClickListener
        val times:Array<RVModel> = generateTimes(noteList, date)
        recyclerView.adapter = NoteAdapter(times, object: BtnClickListener{
            override fun onClick(note: Note) {
                val intent =Intent(context, ExtraInfoActivity::class.java).apply{
                    putExtra(NOTE_EXTRA, note.id)
                }
                startActivity(intent)
            }
        })
    }

    // Generates recycle views for each hour
    private fun generateTimes(notes:MutableList<Note>, date:LocalDate):Array<RVModel>{
        val timesArr = Array(24) { i ->
            RVModel(hour_start = i, hour_end = if (i==23) 0 else i+1)
        }

        for (note:Note in notes){
            val dateStart: LocalDateTime = Utils.convertMillisToTime(note.date_start)
            val dateEnd: LocalDateTime = Utils.convertMillisToTime(note.date_end)
            var hourStart = 0
            var hourEnd= 23

            // if the note start date is the same as picked, then the note's hour is picked
            // else the default is 0
            if (date.isEqual(LocalDate.from(dateStart)))
                hourStart = dateStart.hour

            // if the note end date is the same as picked, then the note's hour is picked
            // else the default is 23
            if (date.isEqual(LocalDate.from(dateEnd)))
                // if the note's end time is whole (e.g. 16:00, 17:00) then we don't count this hour
                hourEnd = if (dateEnd.minute == 0)
                    dateEnd.hour - 1
                else
                    dateEnd.hour

            // in case note's end time is 00:00
            if (hourEnd ==-1 )
                continue

            for (i in hourStart..hourEnd)
                timesArr[i].note = note
        }

        return timesArr
    }

    fun onFabBtnClick(){
        startActivity(Intent(applicationContext, CreateNoteActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}