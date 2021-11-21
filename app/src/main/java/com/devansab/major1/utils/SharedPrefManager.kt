package com.devansab.major1.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPrefManager {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences;
        private lateinit var instance: SharedPrefManager;
        private var KEY_AUTH_TOKEN = "sharedPrefAuthToken"
        private var KEY_FCM_TOKEN = "sharedPrefFCMToken"

        fun getInstance(context: Context): SharedPrefManager {
            if (!this::sharedPreferences.isInitialized || !this::instance.isInitialized) {
                instance = SharedPrefManager();
                sharedPreferences = context.getSharedPreferences("Major1", MODE_PRIVATE);
            }
            return instance;
        }
    }

    private fun SharedPrefManager() {}

    public fun setAuthToken(token: String?) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    public fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public fun setFCMToken(token: String) {
        sharedPreferences.edit().putString(KEY_FCM_TOKEN, token).apply()
    }

    public fun getFCMToken(): String? {
        return sharedPreferences.getString(KEY_FCM_TOKEN, null)
    }
}