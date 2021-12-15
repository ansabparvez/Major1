package com.devansab.begnn.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.devansab.begnn.viewmodels.MainViewModel
import com.devansab.begnn.R
import com.devansab.begnn.utils.DebugLog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.myLooper()!!).postDelayed({
            initViews();
        }, 3000)
    }

    private fun initViews(){
        val viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        )[MainViewModel::class.java];

        //FirebaseAuth.getInstance().signOut()
        val user = FirebaseAuth.getInstance().currentUser;
        if (user == null) {
            DebugLog.i(this, "user is null");
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
            finish();
            return;
        }
        DebugLog.i(this, "user is not null");

        viewModel.isUserRegistered()

        viewModel.getRegisterUserLiveData().observe(this, { isRegistered ->
            if (isRegistered) {
                DebugLog.i(this, "user is registered");
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                DebugLog.i(this, "user is not registered");
                val intent = Intent(this, UserRegistrationActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}