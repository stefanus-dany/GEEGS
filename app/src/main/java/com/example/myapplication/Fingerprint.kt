package com.example.myapplication

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.myapplication.companion.Companion
import com.example.myapplication.databinding.ActivityFingerprintBinding

class Fingerprint : AppCompatActivity() {

    private var check = false
    private var cancellationSignal: CancellationSignal? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication success!")
                    startActivity(Intent(this@Fingerprint, MainActivity::class.java))
                    finish()
                }
            }

    private lateinit var binding: ActivityFingerprintBinding

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFingerprintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("sharedPrefs3", Context.MODE_PRIVATE)

        checkBiometricSupport()
        visibleFingerprint()

        binding.ivFingerprint.setOnClickListener {
            visibleFingerprint()
        }

        binding.btnPwdManual.setOnClickListener {
            check = false
            binding.ivFingerprint.visibility = View.GONE
            binding.btnPwdManual.visibility = View.GONE
            binding.consLayout2.visibility = View.VISIBLE
        }
//
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            binding.btnGoToHome.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
//        } else binding.btnGoToHome.backgroundTintList = ColorStateList.valueOf(Color.WHITE)

        binding.btnGoToHome.setOnClickListener {
            if (binding.etPassword.text.toString().trim().isEmpty()) {
                binding.etPassword.error = "Please enter password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            val passwordSettings =
                sharedPreferences.getString(Companion.SAVE_PASSWORD_FINGERPRINT, null)
            if (binding.etPassword.text.toString().trim() == passwordSettings) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (!check) {
            binding.ivFingerprint.visibility = View.VISIBLE
            binding.btnPwdManual.visibility = View.VISIBLE
            binding.consLayout2.visibility = View.GONE
            check = true
        } else super.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun visibleFingerprint() {
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("GEEGS Locked")
            .setSubtitle("Authentication is required")
            .setDescription("GEEGS uses fingerprint protection to keep your data secure")
            .setNegativeButton("Cancel", this.mainExecutor, { _, _ ->
                notifyUser("Authentication cancelled")
            }).build()
        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }
}