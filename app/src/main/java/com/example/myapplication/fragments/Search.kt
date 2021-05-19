package com.example.myapplication.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.RecognizerResultsIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.SongAdapter
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.model.SongModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.util.*

class Search : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SongAdapter
    private lateinit var data: MutableList<SongModel>
    private lateinit var displayData: MutableList<SongModel>
    val REQUEST_CODE_SPEECH_INPUT = 100

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

        binding.btnSpeak.setOnClickListener{
            speak()

        }
    }

    private fun read() {
        data.clear()
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

    private fun speak(){
        val move = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        move.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        move.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        move.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now!")

        try {
            startActivityForResult(move, REQUEST_CODE_SPEECH_INPUT)
        }catch (e: Exception){

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_SPEECH_INPUT->{
                if(resultCode == Activity.RESULT_OK && data != null){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    binding.searchview.setQuery(result?.get(0)?.toString(), false)
                    binding.searchview.isIconified = false
                }
            }
        }
    }
}