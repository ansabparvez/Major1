package com.devansab.begnn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devansab.begnn.data.entities.Message
import com.devansab.begnn.data.repositories.MessageRepository
import com.devansab.begnn.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class KnownUserChatViewModel(application: Application) : AndroidViewModel(application) {
    private val messageRepository = MessageRepository(application)
    private val userRepository = UserRepository(application)

    fun getUserByUsername(username: String) = userRepository.getUserByUsername(username)

    fun getAllMessagesOfUser(username: String, isAnonymous: Boolean): Flow<List<Message>> {
        val anonymous = if (isAnonymous) 1 else 0
        return messageRepository.getAllMessagesOfUser(username, anonymous)
    }

    suspend fun sendMessage(message: Message, name: String) {
        messageRepository.sendMessageToKnownUser(message)
    }
}