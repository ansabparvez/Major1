package com.devansab.major1.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devansab.major1.viewmodles.MainViewModel
import com.devansab.major1.R
import com.devansab.major1.utils.DebugLog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        ).get(MainViewModel::class.java);

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
            }else{
                DebugLog.i(this, "user is not registered");
                val intent = Intent(this, UserRegistrationActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

    }
}