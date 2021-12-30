package com.devansab.begnn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devansab.begnn.data.repositories.UserRepository
import com.devansab.begnn.utils.SharedPrefManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class UserRegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository = UserRepository(application)

    fun isUserNameAvailable(userName: String) {
        userRepository.checkUserNameAvailable(userName)
    }

    fun getUserNameAvailableLiveData(): LiveData<Boolean> {
        return userRepository.getIsUserNameAvailableLiveData()
    }

    fun completeRegistration(name: String, userName: String) {
        val userDataMap = HashMap<String, String>()
        userDataMap["name"] = name
        userDataMap["userName"] = userName
        setFCMTokenAndRegister(userDataMap)

    }

    private fun setFCMTokenAndRegister(userDataMap: HashMap<String, String>) {
        val token = SharedPrefManager.getInstance(getApplication()).getFCMToken()
        if (token != null) {
            userDataMap["fcmToken"] = token
            userRepository.registerUser(userDataMap)
            return
        }

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newToken = task.result
                    userDataMap["fcmToken"] = newToken
                    userRepository.registerUser(userDataMap)
                }
            }
    }

    fun getUserRegistrationLiveData(): LiveData<Boolean> {
        return userRepository.getUserRegistrationLiveData()
    }

}