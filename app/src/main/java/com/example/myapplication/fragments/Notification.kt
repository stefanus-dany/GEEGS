package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.NotificationAdapter
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.FragmentNotificationBinding
import com.example.myapplication.model.NotificationModel
import com.example.myapplication.model.SongModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Notification : Fragment() {
    private lateinit var adapter: NotificationAdapter
    private lateinit var data: MutableList<NotificationModel>
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser

        data = mutableListOf()
        adapter = NotificationAdapter(data)
        adapter.mContext = requireContext()

        binding.recyclerNotification.layoutManager = LinearLayoutManager(context)
        binding.recyclerNotification.adapter = adapter
        init()
    }
    private fun init () {
        data.clear()
        val song = FirebaseDatabase.getInstance().reference.child("Users").child(user.uid).child("notification")
        song.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val value = dataSnapshot.getValue(NotificationModel::class.java)
                    if (value != null) {
                        data.add(value)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}