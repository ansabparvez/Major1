package com.devansab.begnn.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPrefManager private constructor() {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var instance: SharedPrefManager
        private const val KEY_AUTH_TOKEN = "sharedPrefAuthToken"
        private const val KEY_FCM_TOKEN = "sharedPrefFCMToken"
        private const val KEY_USER_NAME = "sharedPrefUserName"
        private const val KEY_USER_UNAME = "sharedPrefUserUName"
        private const val KEY_USER_ANON_ID = "sharedPrefAnonID"

        fun getInstance(context: Context): SharedPrefManager {
            if (!this::sharedPreferences.isInitialized || !this::instance.isInitialized) {
                instance = SharedPrefManager()
                sharedPreferences = context.getSharedPreferences("Begnn", MODE_PRIVATE)
            }
            return instance
        }
    }

    fun setAuthToken(token: String?) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

     fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

     fun setFCMToken(token: String) {
        sharedPreferences.edit().putString(KEY_FCM_TOKEN, token).apply()
    }

     fun getFCMToken(): String? {
        return sharedPreferences.getString(KEY_FCM_TOKEN, null)
    }

     fun setUserData(userData: Map<String, String>) {
        sharedPreferences.edit()
            .putString(KEY_USER_NAME, userData[Const.KEY_USER_NAME])
            .putString(KEY_USER_UNAME, userData[Const.KEY_USER_UNAME])
            .putString(KEY_USER_ANON_ID, userData[Const.KEY_USER_ANON_ID])
            .apply()
    }

     fun getUserData(): Map<String, String> {
        val userData = HashMap<String, String>()
        userData[Const.KEY_USER_NAME] = sharedPreferences.getString(KEY_USER_NAME, "").toString()
        userData[Const.KEY_USER_UNAME] = sharedPreferences.getString(KEY_USER_UNAME, "").toString()
        userData[Const.KEY_USER_ANON_ID] =
            sharedPreferences.getString(KEY_USER_ANON_ID, "").toString()
        return userData
    }
}