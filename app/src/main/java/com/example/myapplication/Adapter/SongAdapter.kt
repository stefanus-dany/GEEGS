package com.example.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ResultListBinding
import com.example.myapplication.model.SongModel

class SongAdapter(val SongData : MutableList<SongModel>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    lateinit var context : Context


    class ViewHolder(val binding: ResultListBinding)  : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ResultListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvSongtitle.text = SongData[position].title
        holder.binding.tvArtist.text = SongData[position].artist
    }

    override fun getItemCount(): Int {
        return SongData.size
    }
}