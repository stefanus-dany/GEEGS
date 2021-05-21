package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.NotificationModel

class NotificationAdapter(val notificationData : MutableList<NotificationModel>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    lateinit var mContext : Context
    inner class ViewHolder (itemview : View): RecyclerView.ViewHolder(itemview){
        var title = itemview.findViewById<TextView>(R.id.tv_notifTitle)
        var desc = itemview.findViewById<TextView>(R.id.tv_notifDesc)
        var iv = itemview.findViewById<ImageView>(R.id.iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.notif_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = "Your song has been added!"
        holder.desc.text = notificationData[position].desc
    }

    override fun getItemCount(): Int {
        return notificationData.size
    }
}