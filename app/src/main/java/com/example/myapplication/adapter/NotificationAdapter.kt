package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemLyricsBinding
import com.example.myapplication.databinding.NotifListBinding
import com.example.myapplication.model.NotificationModel

class NotificationAdapter(val notificationData : MutableList<NotificationModel>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    lateinit var mContext : Context

    inner class ViewHolder (val binding : NotifListBinding): RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NotifListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvNotifTitle.text = "Your song has been added!"
        holder.binding.tvNotifDesc.text = notificationData[position].desc
    }

    override fun getItemCount(): Int {
        return notificationData.size
    }
}