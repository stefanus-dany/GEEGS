package com.example.myapplication.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.myapplication.MainActivity
import com.example.myapplication.Settings
import com.example.myapplication.databinding.FragmentProfileBinding
import com.example.myapplication.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        val profile = FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
        profile.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(UserModel::class.java)
                binding.name.text = value?.fullname
                binding.email.text = value?.email
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.ibSettings.setOnClickListener{
            activity?.startActivity(Intent(context, Settings::class.java))
        }


    }


    override fun onResume() {
        super.onResume()
//        val sp:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//
//        val nightmode = sp.getBoolean("nightmode", false)
//        val notifications = sp.getBoolean("notifications", false)
//
//        //todo
//        //notifications
//
//        //nightmode
//        if (nightmode){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }else{
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
    }

}