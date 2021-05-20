package com.example.myapplication

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

    private lateinit var binding: ActivityMainBinding
    private var goToProfile: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("errorProfile", "onCreate: errorProfile")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //deklarasi dan inisiasi kelas
        val homefrag = Home()
        val searchfrag = Search()
        val notificationfrag = Notification()
        val profilefrag = Profile()
        val fromaddsong = intent.getBooleanExtra(Companion.MOVE_FROM_ADDSONG_TO_NOTIFICATION, false)

        Log.i("muh", "onCreate: "+fromaddsong)
        if (fromaddsong){
            makeCurrentFragment(notificationfrag)
        }

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
//        val notifications = sp.getBoolean("notifications", false)

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
                goToProfile = true
            }
            null -> {
                binding.bottomNavigation.selectedItemId = R.id.search_icon
            }
            else -> {
                binding.bottomNavigation.selectedItemId = R.id.profile_icon
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