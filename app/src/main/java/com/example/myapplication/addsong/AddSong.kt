package com.example.myapplication.addsong

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.ContentView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.ActivityAddSongBinding
import com.example.myapplication.fragments.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AddSong : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityAddSongBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var userId: String
    private lateinit var spinner: Spinner
    private lateinit var genre: String

    private val CHANNEL_ID = "com.example.myapplication.addsong"
    private val notificationId = 101

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        spinner = binding.listgenre
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.musicgenre_arrays,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.listgenre.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
            binding.etLyrics.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)

        }

        //create notification
        createNotificationChannel()


        binding.btnSumbitSong.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            if (binding.etBy.text.toString().trim().isEmpty()) {
                binding.etBy.error = "Please enter Primary Artist, Author, Creator, etc"
                binding.etBy.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.etTitle.text.toString().trim().isEmpty()) {
                binding.etTitle.error = "Please enter SongModel Title"
                binding.etTitle.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }
            //dropdown genre
            if (genre == "Choose song genre") {
                Toast.makeText(this, "Input song genre", Toast.LENGTH_SHORT).show()
                binding.listgenre.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.etLyrics.text.toString().trim().isEmpty()) {
                binding.etLyrics.error = "Please enter Lyrics"
                binding.etLyrics.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.etUrl.text.toString().trim().isEmpty()) {
                binding.etUrl.error = "Please enter Youtube URL"
                binding.etUrl.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            user = auth.currentUser as FirebaseUser
            userId = user.uid

            val reference = FirebaseDatabase.getInstance().reference.child("Songs")
                .child(userId).child(binding.etTitle.text.toString())

            val hashMap = HashMap<String, String>()
            hashMap["artist"] = binding.etBy.text.toString()
            hashMap["title"] = binding.etTitle.text.toString()
            hashMap["lyrics"] = binding.etLyrics.text.toString()
            hashMap["url"] = binding.etUrl.text.toString()

            reference.setValue(hashMap).addOnCompleteListener {
                if (it.isSuccessful) {
                    val reference = FirebaseDatabase.getInstance().reference.child("ListSong")
                        .child(binding.etTitle.text.toString())

                    val hashMap = HashMap<String, String>()
                    hashMap["artist"] = binding.etBy.text.toString()
                    hashMap["title"] = binding.etTitle.text.toString()
                    hashMap["genre"] = genre
                    hashMap["lyrics"] = binding.etLyrics.text.toString()
                    hashMap["url"] = binding.etUrl.text.toString()
                    hashMap["count"] = 0.toString()

                    reference.setValue(hashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.progressBar.visibility = View.INVISIBLE
                        } else {
                            Toast.makeText(this, "Database failed", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Song lyrics has been added.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    sendNotification()
                    finish()
                } else {
                    Toast.makeText(this, "Database failed", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(Companion.MOVE_FROM_ADDSONG_TO_NOTIFICATION, true)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val bitmapLargeIcon =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.logo_geegs)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_geegs)
            .setContentTitle("Your song has been added!")
            .setContentText("Congratulation your lyrics has been added to Geegs! Thank you for your contributions and let's sing together!")
            .setLargeIcon(bitmapLargeIcon)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Congratulation your lyrics has been added to Geegs! Thank you for your contributions and let's sing together!"))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
        genre = text
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}