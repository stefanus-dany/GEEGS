package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.fragments.Home
import com.example.myapplication.fragments.Notification
import com.example.myapplication.fragments.Profile
import com.example.myapplication.fragments.Search

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private var goToProfile: Boolean? = false
    private var fromAddSong = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("errorProfile", "onCreate: errorProfile")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("sharedPrefs4", Context.MODE_PRIVATE)

        //deklarasi dan inisiasi kelas
        val homefrag = Home()
        val searchfrag = Search()
        val notificationfrag = Notification()
        val profilefrag = Profile()

        //mengubah fragment saat icon di klik
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_icon -> {
                    makeCurrentFragment(homefrag)
                    goToProfile = false
                }
                R.id.search_icon -> {
                    makeCurrentFragment(searchfrag)
                    goToProfile = null
                }
                R.id.notification_icon -> {
                    makeCurrentFragment(
                        notificationfrag,
                    )
                    goToProfile = null
                }
                R.id.profile_icon -> {
                    makeCurrentFragment(profilefrag)
                    goToProfile = true
                }
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_frame, fragment)
            commit()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("errorProfile", "onStart: errorProfile")
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val nightmode = sp.getBoolean("nightmode", false)

        //nightmode
        if (nightmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("errorProfile", "onResume: errorProfile")
        when (goToProfile) {
            false -> {
                binding.bottomNavigation.selectedItemId = R.id.home_icon
            }
            null -> {
                binding.bottomNavigation.selectedItemId = R.id.search_icon
            }
            else -> {
                binding.bottomNavigation.selectedItemId = R.id.profile_icon
            }
        }

        val getSendNotif = sharedPreferences.getBoolean(Companion.CLICK_NOTIF, false)
        fromAddSong = intent.getBooleanExtra(Companion.FROM_ADDSONG_TO_MAIN, false)

        if (getSendNotif) {
            if (!fromAddSong) {
                binding.bottomNavigation.selectedItemId = R.id.notification_icon
                val editor = sharedPreferences.edit()
                editor.clear().apply()
            }
        }


    }

    override fun onPause() {
        super.onPause()
        Log.i("errorProfile", "onPause: errorProfile")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("errorProfile", "onDestroy: errorProfile")
    }

    override fun onStop() {
        super.onStop()
        Log.i("errorProfile", "onStop: errorProfile")
    }
}