package com.myproject.notebook.recycler_view

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.myproject.notebook.R
import com.myproject.notebook.models.Note
import com.myproject.notebook.service.Utils

class NoteAdapter(
    private val timesOfDay: Array<RVModel>,
    private val onClickListener: BtnClickListener
):RecyclerView.Adapter<NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_card, parent, false);
        return NoteViewHolder(viewItem);
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val time = timesOfDay[position]
        "${time.hour_start}:00-${time.hour_end}:00".also { holder.timeTextView.text = it }
        val note = time.note
        if (note == null){
            "No task".also { holder.titleTextView.text = it }
            holder.moreInfoBtn.visibility = View.GONE
        }else{
            holder.titleTextView.text = note.name
            holder.moreInfoBtn.visibility = View.VISIBLE
            holder.moreInfoBtn.setOnClickListener { onClickListener.onClick(note) }
        }
//        val note = notes[position]
//        val (_, dateStartMilli, dateEndMilli, name, _) = note
//        holder.titleTextView.text = name
//
//        val dateStart = Utils.convertTimeStart(dateStartMilli)
//        val dateEnd = Utils.convertTimeStart(dateEndMilli)
//
//        holder.timeTextView.text = "${dateStart.hour}:${dateEnd.hour}"
//        holder.moreInfoBtn.setOnClickListener { object :View.OnClickListener{
//            override fun onClick(p0: View?) {
//
//            }
//
//        } }
    }

    override fun getItemCount(): Int = timesOfDay.size

}