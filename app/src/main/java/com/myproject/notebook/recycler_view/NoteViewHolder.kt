package com.myproject.notebook.recycler_view

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myproject.notebook.R

class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val titleTextView: TextView = itemView.findViewById(R.id.card_title)
    val timeTextView: TextView = itemView.findViewById(R.id.card_time)

    val moreInfoBtn: Button = itemView.findViewById(R.id.more_info_btn)
}