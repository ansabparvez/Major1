package com.devansab.begnn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devansab.begnn.data.repositories.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository = UserRepository(application)

    fun isUserRegistered() {
        userRepository.isUserRegistered()
    }

    fun getRegisterUserLiveData(): LiveData<Boolean> {
        return userRepository.getRegisterUserLiveData()
    }

    fun updateFcmToken() {
        userRepository.fetchAndUpdateFcmToken()
    }

}