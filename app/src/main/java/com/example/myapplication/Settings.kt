package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.google.firebase.auth.FirebaseAuth

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val button = findPreference<Preference>("logout")
            val fingerprint = findPreference<SwitchPreference>("password")
            button!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                FirebaseAuth.getInstance().signOut()
                MainActivity().finish()
                val move = Intent(context, Login::class.java)
                move.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(Intent(move))
                true
            }

            fingerprint!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

                val password = sp.getBoolean("password", false)

                if (password){
                    startActivity(Intent(context, SetPasswordApp::class.java))
                }
                true
            }
        }
    }

}