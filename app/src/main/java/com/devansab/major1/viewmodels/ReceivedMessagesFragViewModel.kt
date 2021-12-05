package com.devansab.major1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devansab.major1.data.entities.LastMessage
import com.devansab.major1.data.entities.User
import com.devansab.major1.data.repositories.MessageRepository
import com.devansab.major1.data.repositories.UserRepository
import com.devansab.major1.utils.DebugLog
import kotlinx.coroutines.flow.Flow

class ReceivedMessagesFragViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)
    private val messageRepository = MessageRepository(application)

    fun getFindUserLiveData(): LiveData<UserRepository.FindUserModel> {
        return userRepository.getFindUserLiveData()
    }

    fun getAllAnonymousLastMessages() :
            Flow<List<LastMessage>> = messageRepository.getAllUnAnonymousLastMessages()

    suspend fun insertUser(user: User) {
        userRepository.insertUser(user)
    }
}