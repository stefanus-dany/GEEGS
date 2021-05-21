package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.LyricsAdapter
import com.example.myapplication.addsong.AddSong
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.model.LyricsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var adapter : LyricsAdapter
    lateinit var data : MutableList<LyricsModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddsong.setOnClickListener {
            startActivity(Intent(context, AddSong::class.java))
        }
        data = mutableListOf()
        adapter = LyricsAdapter(data)
        adapter.mContext = requireContext()

        binding.recyclerChart.layoutManager = LinearLayoutManager(context)
        binding.recyclerChart.adapter = adapter
        init()
    }

    private fun init () {
        data.clear()
        val song = FirebaseDatabase.getInstance().reference.child("ListSong")
        song.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val value = dataSnapshot.getValue(LyricsModel::class.java)
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