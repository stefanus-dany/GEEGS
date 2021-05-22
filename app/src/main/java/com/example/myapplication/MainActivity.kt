package com.example.myapplication

import android.content.Context
import android.content.Intent
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
    private var goToSearch = false
    private var runOnResume = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("errorProfile", "onCreate: errorProfile")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("sharedPrefSearch", Context.MODE_PRIVATE)
        goToSearch = sharedPreferences.getBoolean(Companion.GO_TO_SEARCH, false)
        if (savedInstanceState != null) {
            goToSearch = savedInstanceState.getBoolean(Companion.GO_TO_SEARCH_MAIN, false)
            runOnResume = false
        } else runOnResume = true

        Log.i("cekGoTO", "gotosearch $goToSearch")
        if (goToSearch){

            binding.bottomNavigation.selectedItemId = R.id.search_icon
        }

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
        if (runOnResume) {
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
        }

        //if notic clicked, then go to notif frag
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        if (extras != null) {
            if (extras.containsKey(Companion.CLICK_NOTIF)) {
                binding.bottomNavigation.selectedItemId = R.id.notification_icon
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Companion.GO_TO_SEARCH_MAIN, goToSearch)
    }
}