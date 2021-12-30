package com.devansab.begnn.utils

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugLog.i("main application", "main application started")
        instance = this
        requestQueue = Volley.newRequestQueue(applicationContext)
        addAuthTokenChangeListener()
        createNotificationChannels()
    }

    companion object {
        lateinit var instance: MainApplication
            private set
        private lateinit var requestQueue: RequestQueue
    }

    fun addToRequestQueue(request: JsonObjectRequest) {
        requestQueue.add(request)
    }

    private fun createNotificationChannels() {
        MyNotificationManager.createNotificationChannel(
            MyNotificationManager.MESSAGE_NOTIFICATION_CHANNEL,
            MyNotificationManager.MESSAGE_NOTIFICATION_CHANNEL_DESC,
            MyNotificationManager.MESSAGE_NOTIFICATION_IMPORTANCE
        )
    }

    private fun addAuthTokenChangeListener() {
        DebugLog.i("main application", "adding token listener")
        FirebaseAuth.getInstance().currentUser ?: return

//        FirebaseAuth.getInstance().addIdTokenListener(FirebaseAuth.IdTokenListener { it ->
//            it.getAccessToken(true).addOnSuccessListener { res ->
//                val token = res.token
//                DebugLog.i("ansab", "token listener: $token")
//                //SharedPrefManager.getInstance(this).setAuthToken(token)
//            }
//        })
    }
}