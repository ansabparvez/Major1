package com.devansab.major1.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPrefManager {

    companion object {
        private var instance: SharedPreferences? = null;
        private var KEY_AUTH_TOKEN = "sharedPrefAuthToken"

        fun getInstance(context: Context): SharedPreferences? {
            if (instance == null)
                instance = context.getSharedPreferences("Major1", MODE_PRIVATE);
            return instance;
        }
    }

    private fun SharedPrefManager() {}

    public fun setAuthToken(token: String) {
        if (instance == null)
            return
        instance!!.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    public fun getAuthToken(): String? {
        return instance?.getString(KEY_AUTH_TOKEN, null);
    }
}