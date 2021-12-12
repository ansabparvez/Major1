package com.devansab.begnn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devansab.begnn.data.entities.LastMessage
import com.devansab.begnn.data.entities.User
import com.devansab.begnn.data.repositories.LastMessageRepository
import com.devansab.begnn.data.repositories.MessageRepository
import com.devansab.begnn.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class UnknownChatLastMsgsViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)
    private val messageRepository = MessageRepository(application)
    private val lastMessageRepository = LastMessageRepository(application)

    fun getFindUserLiveData(): LiveData<UserRepository.FindUserModel> {
        return userRepository.getFindUserLiveData()
    }

    fun getAllAnonymousLastMessages():
    //Not anonymous, so provide 0 for room query
            Flow<List<LastMessage>> = lastMessageRepository.getAllLastMessages(1)

    suspend fun insertUser(user: User) {
        userRepository.insertUser(user)
    }
}