package com.myproject.notebook.recycler_view

import com.myproject.notebook.models.Note

interface BtnClickListener {
    fun onClick(note: Note)
}