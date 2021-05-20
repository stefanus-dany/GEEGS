package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemLyricsBinding
import com.example.myapplication.model.LyricsModel

class LyricsAdapter (val dataLyrics : MutableList<LyricsModel>) : RecyclerView.Adapter<LyricsAdapter.ViewHolder>() {
    lateinit var  mContext : Context
    inner class ViewHolder (val binding: ItemLyricsBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLyricsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.lyrics.text = dataLyrics[position].title
        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return dataLyrics.size
    }
}