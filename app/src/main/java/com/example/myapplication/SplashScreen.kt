package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.myapplication.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNightMode()

        binding.ivSplash.alpha = 0f
        binding.descText.alpha = 0f
        binding.descText.animate().setDuration(1500).alpha(1f).withEndAction{
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding.ivSplash.animate().setDuration(1500).alpha(1f).withEndAction{
            startActivity(Intent(this, Login::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

    }

    fun checkNightMode(){
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val nightmode = sp.getBoolean("nightmode", false)
        val notifications = sp.getBoolean("notifications", false)

        //nightmode
        if (nightmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}