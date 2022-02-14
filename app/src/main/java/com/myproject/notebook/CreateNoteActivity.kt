package com.myproject.notebook

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.myproject.notebook.models.Note
import com.myproject.notebook.service.Utils
import io.realm.Realm
import io.realm.kotlin.createObject
import java.time.LocalDateTime
import java.util.*

class CreateNoteActivity : AppCompatActivity() {
    lateinit var realm:Realm
    lateinit var dateStart: LocalDateTime
    lateinit var dateEnd:LocalDateTime
    var nameText: String = ""
    var descriptionText: String = ""

    val START = "START"
    val END = "END"

    override fun onCreate(savedInstanceState: Bundle?) {
        realm = Realm.getDefaultInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        fillViews()
    }

    private fun fillViews() {
        findViewById<Button>(R.id.create_btn_date_start).setOnClickListener{pickDateTime(START)}
        findViewById<Button>(R.id.create_btn_date_end).setOnClickListener{pickDateTime(END)}

        findViewById<Button>(R.id.create_btn).setOnClickListener { onCreateBtnClick() }
    }

    // Opens date and time picker dialogs
    private fun pickDateTime(type:String) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(
            this@CreateNoteActivity,
            { _, year, month, day ->
                TimePickerDialog(
                    this@CreateNoteActivity,
                    { _, hour, minute ->
                        val datetime = LocalDateTime.of(year, month+1, day, hour, minute)
                        val dateStr = Utils.formatDate(datetime)

                        // if the "Select start date" button was clicked
                        if (type == START) {
                            dateStart = datetime
                            findViewById<TextView>(R.id.create_textview_date_start).text = dateStr
                        }
                        else {
                            dateEnd = datetime
                            findViewById<TextView>(R.id.create_textview_date_end).text = dateStr
                        }

                    },
                    startHour,
                    startMinute,
                    false
                ).show()
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }

    private fun onCreateBtnClick(){
        nameText = findViewById<EditText>(R.id.create_textview_name).text.toString()
        descriptionText = findViewById<EditText>(R.id.create_textview_description).text.toString()

        val errStr:String = validateFields()
        if (errStr == ""){

            // Create new note and add to database
            var note:Note
            realm.executeTransactionAsync { realm_ ->
                note = realm_.createObject(Note::class.java, UUID.randomUUID().toString())
                note.description = descriptionText
                note.name = nameText
                note.date_end = Utils.convertTimeToMillis(dateEnd)
                note.date_start = Utils.convertTimeToMillis(dateStart)
            }
            this@CreateNoteActivity.finish()
        }else{
            Toast.makeText(applicationContext, errStr, Toast.LENGTH_LONG).show()
        }
    }

    private fun validateFields():String{

        if (nameText.equals(""))
            return "Name is empty"
        if (descriptionText.equals(""))
            return "Description is empty"
        if (!this::dateStart.isInitialized)
            return "Start date is empty"
        if (!this::dateEnd.isInitialized)
            return "End date is empty"
        if (dateStart.isAfter(dateEnd))
            return "End date cannot be before start date"
        if (dateStart == dateEnd)
            return "Start and end dates cannot be the same "

        return ""
    }
}