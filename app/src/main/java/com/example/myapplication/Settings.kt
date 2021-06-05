package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.example.myapplication.companion.Companion
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

        private lateinit var button : Preference
        private lateinit var fingerprint : SwitchPreference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            button = findPreference<Preference>("logout") as Preference
            fingerprint = findPreference<SwitchPreference>("password") as SwitchPreference
            button.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                FirebaseAuth.getInstance().signOut()
                MainActivity().finish()
                val move = Intent(context, Login::class.java)
                move.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(Intent(move))
                true
            }

            fingerprint.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

                val password = sp.getBoolean("password", false)

                if (password){
                    val move = Intent(context, SetPasswordApp::class.java)
                    startActivityForResult(move, Companion.RESULTCODE_FROM_SETPASSWORD_TO_PROFILE)
                }
                true
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            Log.i("ope", "requ code: $requestCode")
            Log.i("ope", "result code: $resultCode")
            if (requestCode == Companion.RESULTCODE_FROM_SETPASSWORD_TO_PROFILE) {
                if (resultCode == Companion.RESULTCODE_FROM_SETPASSWORD_TO_PROFILE) {
                    val selectedValue =
                        data?.getBooleanExtra(Companion.RESULT_FROM_SETPASSWORD_TO_PROFILE, false)
                    Log.i("ope", "value in settings: $selectedValue")
                    if (selectedValue == true) {
                        fingerprint.isChecked = true
                    }
                } else fingerprint.isChecked = false
            }
        }
    }

}