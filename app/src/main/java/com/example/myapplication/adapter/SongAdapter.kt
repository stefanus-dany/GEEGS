package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailLyric
import com.example.myapplication.companion.companion
import com.example.myapplication.databinding.ResultListBinding
import com.example.myapplication.model.SongModel

class SongAdapter(val SongData : MutableList<SongModel>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    lateinit var context : Context

    class ViewHolder(val binding: ResultListBinding)  : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ResultListBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvSongtitle.text = SongData[position].title
        holder.binding.tvArtist.text = SongData[position].artist
        holder.itemView.setOnClickListener {
            val move = Intent(context, DetailLyric::class.java)
            move.putExtra(companion.TITLE_DATA, SongData[position].title)
            context.startActivity(move)
        }
    }

    override fun getItemCount(): Int {
        return SongData.size
    }
}