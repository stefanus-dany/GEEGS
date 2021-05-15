package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.Fragments.Home
import com.example.myapplication.Fragments.Notification
import com.example.myapplication.Fragments.Profile
import com.example.myapplication.Fragments.Search
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //deklarasi dan inisiasi kelas
        val homefrag = Home()
        val searchfrag = Search()
        val notificationfrag = Notification()
        val profilefrag = Profile()
        val profilefrag2 = Profile()

        makeCurrentFragment(homefrag, R.id.home_icon)

        //mengubah fragment saat icon di klik
        binding.bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home_icon -> makeCurrentFragment(homefrag, R.id.home_icon)
                R.id.search_icon -> makeCurrentFragment(searchfrag, R.id.search_icon)
                R.id.notification_icon -> makeCurrentFragment(notificationfrag, R.id.notification_icon)
                R.id.profile_icon -> makeCurrentFragment(profilefrag, R.id.profile_icon)
            }
            true
        }

    }

    fun makeCurrentFragment(fragment : Fragment, item : Int){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_frame, fragment)
            commit()
        }
    }
}