package com.myproject.notebook.recycler_view

import com.myproject.notebook.models.Note

data class RVModel (
    val hour_start: Int,
    val hour_end: Int,
    var note: Note? = null
        ){

}