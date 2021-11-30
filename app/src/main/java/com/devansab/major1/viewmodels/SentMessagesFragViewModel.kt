package com.devansab.major1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devansab.major1.data.entities.LastMessage
import com.devansab.major1.data.repositories.MessageRepository
import com.devansab.major1.data.repositories.UserRepository
import com.devansab.major1.utils.DebugLog
import kotlinx.coroutines.flow.Flow

class SentMessagesFragViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)
    private val messageRepository = MessageRepository(application)

    fun findUser(userName: String) {
        DebugLog.i(this, "user to search: $userName")
        userRepository.findUser(userName)
    }

    fun getFindUserLiveData(): LiveData<UserRepository.FindUserModel> {
        return userRepository.getFindUserLiveData()
    }

    fun getAllLastMessages() : Flow<List<LastMessage>> = messageRepository.getAllLastMessages()
}