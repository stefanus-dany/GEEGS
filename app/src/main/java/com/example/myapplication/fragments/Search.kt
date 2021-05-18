package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.SongAdapter
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.model.SongModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class Search : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SongAdapter
    private lateinit var data: MutableList<SongModel>
    private lateinit var displayData: MutableList<SongModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = mutableListOf()
        displayData = mutableListOf()
        read()
        adapter = SongAdapter(displayData)
        adapter.context = requireContext()
        binding.rvSearchlist.layoutManager = LinearLayoutManager(context)
        binding.rvSearchlist.adapter = adapter
        search()
    }

    private fun read() {
        val song = FirebaseDatabase.getInstance().reference.child("ListSong").orderByValue()
        song.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val value = dataSnapshot.getValue(SongModel::class.java)
                    if (value != null) {
                        data.add(value)
                        adapter.notifyDataSetChanged()
                    }
                }
                displayData.addAll(data)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun search() {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    displayData.clear()
                    val search = newText.toLowerCase(Locale.getDefault())
                    data.forEach {
                        if (it.title.toLowerCase(Locale.getDefault()).contains(search) ||
                            it.artist.toLowerCase(Locale.getDefault()).contains(search)) {
                            displayData.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    displayData.clear()
                    displayData.addAll(data)
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }

    override fun onPause() {
        super.onPause()
        binding.searchview.setQuery("", false)
    }
}