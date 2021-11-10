package com.devansab.major1.utils

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    private fun addAuthTokenChangeListener(){
        val user = FirebaseAuth.getInstance().currentUser ?: return;

        FirebaseAuth.getInstance().addIdTokenListener(FirebaseAuth.IdTokenListener { it ->
            it.getAccessToken(true).addOnSuccessListener { res ->
                val token = res.token;
            }
        })
    }
}