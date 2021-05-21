package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailLyric
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.ItemLyricsBinding
import com.example.myapplication.model.LyricsModel
import com.google.firebase.database.FirebaseDatabase

class LyricsAdapter(val dataLyrics: MutableList<LyricsModel>) :
    RecyclerView.Adapter<LyricsAdapter.ViewHolder>() {
    lateinit var mContext: Context

    inner class ViewHolder(val binding: ItemLyricsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLyricsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.lyrics.text = "${position + 1}. ${dataLyrics[position].title}"
        //underline text
        holder.binding.lyrics.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        holder.itemView.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().reference.child("ListSong")
                .child(dataLyrics[position].title).child("count")
            val temp = (dataLyrics[position].count) + 1
            ref.setValue(temp)
            val move = Intent(mContext, DetailLyric::class.java)
            move.putExtra(Companion.TITLE_DATA, dataLyrics[position].title)
            mContext.startActivity(move)
        }
    }

    override fun getItemCount(): Int {
        return dataLyrics.size
    }
}