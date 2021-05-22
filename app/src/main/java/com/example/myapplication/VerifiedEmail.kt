package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.ActivityVerifiedEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.concurrent.Executors

class VerifiedEmail : AppCompatActivity() {

    lateinit var binding: ActivityVerifiedEmailBinding
    private lateinit var user: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private var bgthread: Thread? = null
    private var resendCodeTime = 59

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifiedEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        val moveFromLoginToRegister =
            intent.getBooleanExtra(Companion.MOVE_FROM_LOGIN_TO_VERIFIED_EMAIL, false)
        if (moveFromLoginToRegister) {
            binding.email.visibility = View.INVISIBLE
            binding.textTimer.visibility = View.GONE
            binding.timer.visibility = View.GONE
            binding.btnResendCode.visibility = View.VISIBLE
        } else startTimer()
        val email = intent.getStringExtra(Companion.EMAIL_SENT)
        binding.email.text = "Verification email has been sent to $email"

        binding.btnGoToLogin.setOnClickListener {
            val move = Intent(this, Login::class.java)
            move.putExtra(Companion.MOVE_FROM_VERIFIED_EMAIL_TO_LOGIN, true)
            startActivity(move)
            finish()
        }

        binding.btnResendCode.setOnClickListener {
            Log.i(Companion.TAG, "userVerified: ${user.isEmailVerified}")

            user.sendEmailVerification().addOnSuccessListener {
                binding.textTimer.visibility = View.VISIBLE
                binding.timer.visibility = View.VISIBLE
                binding.btnResendCode.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Resend verification email has been sent.",
                    Toast.LENGTH_SHORT
                ).show()
                startTimer()

            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Resend verification failed. Please try again later",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(Companion.TAG, "onFailure: Resend email not sent " + it.message)
            }
        }

    }

    private fun startTimer() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                while (resendCodeTime != 0) {
                    // Simulating something timeconsuming
                    Thread.sleep(1000) // in milisecond
                    binding.timer.post {
                        binding.timer.text = resendCodeTime.toString()
                    }
                    resendCodeTime--
                }
                resendCodeTime = 59


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                bgthread = Thread()
                bgthread?.start()
                binding.textTimer.visibility = View.GONE
                binding.timer.visibility = View.GONE
                binding.btnResendCode.visibility = View.VISIBLE
            }
        }
    }
}