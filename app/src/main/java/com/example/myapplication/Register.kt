package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var userId: String

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.email.setTextColor(R.color.black)
            binding.email.setHintTextColor(R.color.black)
            binding.fullnameRegister.setTextColor(R.color.black)
            binding.fullnameRegister.setHintTextColor(R.color.black)
            binding.password.setTextColor(R.color.black)
            binding.password.setHintTextColor(R.color.black)
        }

        binding.btnSignup.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            if (binding.email.text.toString().trim().isEmpty()) {
                binding.email.error = "Please enter email"
                binding.email.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.fullnameRegister.text.toString().trim().isEmpty()) {
                binding.fullnameRegister.error = "Please enter fullname"
                binding.fullnameRegister.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString().trim()).matches()) {
                binding.email.error = "Please enter valid email"
                binding.email.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.password.text.toString().trim().isEmpty()) {
                binding.password.error = "Please enter password"
                binding.password.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.password.text.toString().trim().length < 6) {
                binding.password.error = "Password less than 6 character"
                binding.password.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //get user
                        user = auth.currentUser as FirebaseUser
                        //get user ID
                        userId = user.uid
                        user.sendEmailVerification().addOnSuccessListener {
                            val move = Intent(this, VerifiedEmail::class.java)
                            move.putExtra(Companion.EMAIL_SENT, binding.email.text.toString())
                            move.putExtra(Companion.CURRENT_USER, user)
                            Log.i(Companion.TAG, "userRegister: $user")
                            startActivity(move)
                            finish()
                        }.addOnFailureListener {
                            Log.d(Companion.TAG, "onFailure: Email not sent" + it.message)
                        }

                        //simpan di database Users
                        val reference = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(userId)

                        val hashMap = HashMap<String, String>()
                        hashMap["id"] = userId
                        hashMap["email"] = binding.email.text.toString()
                        hashMap["fullname"] = binding.fullnameRegister.text.toString()

                        reference.setValue(hashMap).addOnCompleteListener {
                            if (task.isSuccessful) {
                                binding.progressBar.visibility = View.INVISIBLE
                                reference.child("idCountNotif").setValue(0)
                            } else {
                                Toast.makeText(this, "Database failed", Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.INVISIBLE
                            }
                        }


                    } else {
                        Toast.makeText(
                            baseContext, "Email has been registered.",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
        }

    }

}