package com.devansab.major1.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.devansab.major1.R
import com.devansab.major1.views.fragments.ReceivedMessagesFragment
import com.devansab.major1.views.fragments.SentMessagesFragment
import com.devansab.major1.views.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
    }

    private fun initView(){

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav_home_nav);
        bottomNav.itemIconTintList = null;


            val frag = SentMessagesFragment()
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout_home_frame,
                frag).commit()

        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_home_sent -> {
                    val frag = SentMessagesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout_home_frame,
                    frag).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_home_received -> {
                    val frag = ReceivedMessagesFragment()
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
    }
}