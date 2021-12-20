package com.devansab.begnn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devansab.begnn.data.LastMessage
import com.devansab.begnn.data.entities.User
import com.devansab.begnn.data.repositories.LastMessageRepository
import com.devansab.begnn.data.repositories.MessageRepository
import com.devansab.begnn.data.repositories.UserRepository
import com.devansab.begnn.utils.DebugLog
import kotlinx.coroutines.flow.Flow

class KnownChatLastMsgsViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)
    private val messageRepository = MessageRepository(application)
    private val lastMessageRepository = LastMessageRepository(application)

    fun findUser(userName: String) {
        DebugLog.i(this, "user to search: $userName")
        userRepository.findUser(userName)
    }

    fun getFindUserLiveData(): LiveData<UserRepository.FindUserModel> {
        return userRepository.getFindUserLiveData()
    }

    fun getAllUnAnonymousLastMessages():
    //Not anonymous, so provide 0 for room query
            Flow<List<LastMessage>> = messageRepository.getAllLastMessages(0)

    suspend fun insertUser(user: User) {
        userRepository.insertUser(user)
    }
}