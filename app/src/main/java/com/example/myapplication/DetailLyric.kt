package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.companion.companion
import com.example.myapplication.databinding.ActivityDetailLyricBinding
import com.example.myapplication.model.SongModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailLyric : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLyricBinding
    private lateinit var getTitle: String
    var count = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLyricBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getTitle = intent.getStringExtra(companion.TITLE_DATA) as String
        getLyrics()

        binding.tvUrl.setOnClickListener {
            if (!count) {
                binding.ll.setPadding(0, 0, 0, 0)
                binding.webView.visibility = View.VISIBLE
                binding.webView.webViewClient = WebViewClient()
                binding.webView.apply {
                    loadUrl(binding.tvUrl.text.toString())
                    settings.javaScriptEnabled = true

                }
                count = true
            } else {
                binding.ll.setPadding(0, 0, 0, 0)
                binding.webView.visibility = View.VISIBLE
            }
        }
    }

    private fun getLyrics() {
        val reference = FirebaseDatabase.getInstance().reference.child("ListSong").child(getTitle)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(SongModel::class.java)
                binding.title.text = value?.title
                binding.artist.text = "by ${value?.artist}"
                binding.genre.text = "Genre    :  ${value?.genre}"
                binding.tvUrl.text = value?.url
                binding.lyric.text = value?.lyrics
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onBackPressed() {
        if (binding.webView.visibility == View.GONE) {
            super.onBackPressed()
        }
        binding.ll.setPadding(10, 10, 10, 10)
        binding.webView.visibility = View.GONE
    }
}