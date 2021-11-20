package com.devansab.major1.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devansab.major1.MainViewModel
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
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
            finish();
            return;
        }
        
        viewModel.isUserRegistered()

        viewModel.getRegisterUserLiveData().observe(this, Observer { isRegistered ->
            if (isRegistered) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

    }
}