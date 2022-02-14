package com.myproject.notebook.service

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myproject.notebook.models.Note
import java.io.File
import java.io.IOException

class DataGetter {


    companion object{
        fun getStrFromJson(c:Context):String?{
            return try {
                val instr = c.assets.open("data.json")
                val size = instr.available()
                val buffer = ByteArray(size)
                instr.read(buffer)
                instr.close()
                String(buffer, Charsets.UTF_8)
            }catch (e:IOException){
                e.printStackTrace();
                null;
            }
        }
    }


//    private val jsonString: String = File("data.json").readText(Charsets.UTF_8)

    fun parseData(str: String): MutableList<Note> {
        val gson = Gson();
        val listType = object : TypeToken<MutableList<Note>>() {}.type
        return gson.fromJson(str, listType)
    }
}