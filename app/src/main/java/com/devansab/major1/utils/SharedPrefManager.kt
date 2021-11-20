package com.devansab.major1.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPrefManager {

    companion object {
        private var sharedPreferences: SharedPreferences? = null;
        private var instance : SharedPrefManager? = null;
        private var KEY_AUTH_TOKEN = "sharedPrefAuthToken"

        fun getInstance(context: Context): SharedPrefManager? {
            if (sharedPreferences == null || instance == null) {
                instance = SharedPrefManager();
                sharedPreferences = context.getSharedPreferences("Major1", MODE_PRIVATE);
            }
            return instance;
        }
    }

    private fun SharedPrefManager() {}

    public fun setAuthToken(token: String?) {
        if (sharedPreferences == null)
            return
        sharedPreferences!!.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    public fun getAuthToken(): String? {
        return sharedPreferences?.getString(KEY_AUTH_TOKEN, null);
    }
}