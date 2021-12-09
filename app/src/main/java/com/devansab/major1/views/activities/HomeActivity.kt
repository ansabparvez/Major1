package com.devansab.major1.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devansab.major1.R
import com.devansab.major1.utils.DebugLog
import com.devansab.major1.views.fragments.UnknownChatLastMsgsFragment
import com.devansab.major1.views.fragments.KnownChatLastMsgsFragment
import com.devansab.major1.views.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
    }

    private fun initView(){

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav_home_nav);
        bottomNav.itemIconTintList = null;


            val frag = KnownChatLastMsgsFragment()
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout_home_frame,
                frag).commit()

        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_home_sent -> {
                    val frag = KnownChatLastMsgsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout_home_frame,
                    frag).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_home_received -> {
                    val frag = UnknownChatLastMsgsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout_home_frame,
                    frag).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_home_settings -> {
                    val frag = SettingsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout_home_frame,
                    frag).commit()
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            DebugLog.i("ansab", it)
        }
    }
}