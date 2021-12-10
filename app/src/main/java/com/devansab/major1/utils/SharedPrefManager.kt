package com.devansab.major1.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPrefManager {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences;
        private lateinit var instance: SharedPrefManager;
        private const val KEY_AUTH_TOKEN = "sharedPrefAuthToken"
        private const val KEY_FCM_TOKEN = "sharedPrefFCMToken"
        private const val KEY_USER_NAME = "sharedPrefUserName"
        private const val KEY_USER_UNAME = "sharedPrefUserUName"
        private const val KEY_USER_ANON_ID = "sharedPrefAnonID"

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

    public fun setUserData(userData: Map<String, String>) {
        sharedPreferences.edit()
            .putString(KEY_USER_NAME, userData[Const.KEY_USER_NAME])
            .putString(KEY_USER_UNAME, userData[Const.KEY_USER_UNAME])
            .putString(KEY_USER_ANON_ID, userData[Const.KEY_USER_ANON_ID])
            .apply()
    }

    public fun getUserData(): Map<String, String> {
        val userData = HashMap<String, String>()
        userData[Const.KEY_USER_NAME] = sharedPreferences.getString(KEY_USER_NAME, "").toString()
        userData[Const.KEY_USER_UNAME] = sharedPreferences.getString(KEY_USER_UNAME, "").toString()
        userData[Const.KEY_USER_ANON_ID] =
            sharedPreferences.getString(KEY_USER_ANON_ID, "").toString()
        return userData;
    }
}