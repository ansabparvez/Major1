package com.devansab.major1.viewmodles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devansab.major1.data.repositories.UserRepository
import com.devansab.major1.utils.DebugLog

class SentMessagesFragViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)

    fun findUser(userName: String) {
        DebugLog.i(this, "user to search: $userName")
        userRepository.findUser(userName)
    }

    fun getFindUserLiveData(): LiveData<UserRepository.FindUserModel> {
        return userRepository.getFindUserLiveData()
    }
}