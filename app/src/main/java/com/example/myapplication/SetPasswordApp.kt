package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.ActivitySetPasswordAppBinding

class SetPasswordApp : AppCompatActivity() {

    private lateinit var binding: ActivitySetPasswordAppBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var check = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetPasswordAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("sharedPrefs3", Context.MODE_PRIVATE)

        binding.submitPassword.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            if (binding.password.text.toString().trim().isEmpty()) {
                binding.password.error = "Please enter password"
                binding.password.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.confirmPassword.text.toString().trim().isEmpty()) {
                binding.confirmPassword.error = "Please enter confirm password"
                binding.confirmPassword.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.password.text.toString()
                    .trim() != binding.confirmPassword.text.toString()
            ) {
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
                binding.confirmPassword.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            val editor = sharedPreferences.edit()
            editor.putString(Companion.SAVE_PASSWORD_FINGERPRINT, binding.confirmPassword.text.toString().trim())
            editor.apply()
            binding.progressBar.visibility = View.INVISIBLE
            check = true
            //cek jika layar ditekan diluar dialog maka toggle fingerprint di settingnya off
            val resultIntent = Intent()
            resultIntent.putExtra(Companion.RESULT_FROM_SETPASSWORD_TO_PROFILE, true)
            setResult(Companion.RESULTCODE_FROM_SETPASSWORD_TO_PROFILE, resultIntent)
            finish()
        }
    }
}