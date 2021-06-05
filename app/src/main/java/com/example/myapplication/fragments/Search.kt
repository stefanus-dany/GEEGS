package com.example.myapplication.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.SongAdapter
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.model.SongModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.util.concurrent.Executors

class Search : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SongAdapter
    private lateinit var data: MutableList<SongModel>
    private lateinit var displayData: MutableList<SongModel>
    private var bgthread: Thread? = null
    private var tmp = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("strt", "onCreateView: ")
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.i("strt", "resume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i("strt", "onPause: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("strt", "onViewCreated: ")
        data = mutableListOf()
        displayData = mutableListOf()
        if (savedInstanceState != null) {
            tmp = savedInstanceState.getString(Companion.SAVE_INSTANCE_SEARCH_QUERY).toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()

        Log.i("strt", "onStart: ")
        read()
        adapter = SongAdapter(displayData)
        adapter.context = requireContext()
        binding.rvSearchlist.layoutManager = LinearLayoutManager(context)
        binding.rvSearchlist.adapter = adapter
        search()
        if (tmp!="") loadInstance()
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.searchview.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
        } else binding.searchview.setBackgroundResource(R.drawable.bg_search)
        binding.btnSpeak.setOnClickListener {
            speak()
        }
    }

    private fun loadInstance() {
        val txt = tmp
        binding.searchview.isIconified = false

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                // Simulating something timeconsuming
                Thread.sleep(500) // in milisecond
                binding.searchview.post {
                    binding.searchview.setQuery(txt, false)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                bgthread = Thread()
                bgthread?.start()
            }
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

    //for hide keyboard
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    @SuppressLint("ServiceCast")
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun search() {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                //hide keyboard when press search button on keyboard
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    displayData.clear()
                    val search = newText.toLowerCase(Locale.getDefault())
                    //for save instance
                    tmp = search

                    data.forEach {
                        if (it.title.toLowerCase(Locale.getDefault()).contains(search) ||
                            it.artist.toLowerCase(Locale.getDefault()).contains(search)
                        ) {
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

    override fun onDestroy() {
        super.onDestroy()
        Log.i("strt", "onDestroy:")
        Log.i("cekGoTO", "onDestroy: mau masuk")
        if (binding.searchview.query!="") {
            Log.i("cekGoTO", "onDestroy: masuk")
            val sharedPreferences =
                activity?.getSharedPreferences("sharedPrefSearch", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putBoolean(Companion.GO_TO_SEARCH, true)
            editor?.apply()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.i("strt", "onStop: ")
        data.clear()
        displayData.clear()
        binding.searchview.setQuery("", false)
    }

    private fun speak() {
        val move = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        move.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        move.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        move.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now!")

        try {
            startActivityForResult(move, Companion.REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Companion.REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    binding.searchview.setQuery(result?.get(0)?.toString(), false)
                    binding.searchview.isIconified = false
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Companion.SAVE_INSTANCE_SEARCH_QUERY, tmp)
    }
}