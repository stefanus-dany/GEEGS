package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

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
                            val firebaseUser = auth.currentUser
                            val userId = firebaseUser.uid

                            //simpan di database Users
                            val reference = FirebaseDatabase.getInstance().reference.child("Users")
                                    .child(userId)

                            val hashMap = HashMap<String, String>()
                            hashMap["id"] = userId
                            hashMap["email"] = binding.email.text.toString()
                            hashMap["fullname"] = binding.fullnameRegister.text.toString()

                            reference.setValue(hashMap).addOnCompleteListener {
                                if (task.isSuccessful) {
                                    val move = Intent(this, Login::class.java)
                                    startActivity(move)
                                    finish()
                                    binding.progressBar.visibility = View.INVISIBLE
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
                        }
                    }
        }

    }

}