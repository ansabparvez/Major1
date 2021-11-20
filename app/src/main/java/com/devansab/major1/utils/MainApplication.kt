package com.devansab.major1.utils

import android.app.Application
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth

class MainApplication : Application() {

    private val requestQueue = Volley.newRequestQueue(applicationContext);

    override fun onCreate() {
        super.onCreate()

        addAuthTokenChangeListener()
    }

    companion object{
        private val instance = MainApplication()
        fun getInstance() : MainApplication{
            return instance
        }
    }

    public fun addToRequestQueue(request: JsonObjectRequest) {
        requestQueue.add(request)
    }

    private fun addAuthTokenChangeListener() {
        val user = FirebaseAuth.getInstance().currentUser ?: return;

        FirebaseAuth.getInstance().addIdTokenListener(FirebaseAuth.IdTokenListener { it ->
            it.getAccessToken(true).addOnSuccessListener { res ->
                val token = res.token
                SharedPrefManager.getInstance(this)?.setAuthToken(token);
            }
        })
    }
}