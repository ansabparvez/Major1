package com.devansab.begnn.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devansab.begnn.R
import com.devansab.begnn.utils.Const.Companion.DEEPLINK_SEARCH_USER
import com.devansab.begnn.utils.DebugLog
import com.devansab.begnn.views.fragments.UnknownChatLastMsgsFragment
import com.devansab.begnn.views.fragments.KnownChatLastMsgsFragment
import com.devansab.begnn.views.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

@Suppress("NAME_SHADOWING")
class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
    }

    private fun initView() {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav_home_nav)
        bottomNav.itemIconTintList = null

        var searchUser: String? = null
        if (intent.hasExtra(DEEPLINK_SEARCH_USER))
            searchUser = intent.getStringExtra(DEEPLINK_SEARCH_USER)

        val frag = KnownChatLastMsgsFragment(searchUser = searchUser)
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout_home_frame,
            frag
        ).commit()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home_sent -> {
                    val frag = KnownChatLastMsgsFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout_home_frame,
                        frag
                    ).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_home_received -> {
                    val frag = UnknownChatLastMsgsFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout_home_frame,
                        frag
                    ).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_home_settings -> {
                    val frag = SettingsFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout_home_frame,
                        frag
                    ).commit()
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            DebugLog.i("ansab fcm token", it)
        }
    }
}