package com.devansab.begnn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devansab.begnn.data.entities.Message
import com.devansab.begnn.data.repositories.MessageRepository

class UnknownUserChatViewModel(application: Application) : AndroidViewModel(application) {
    private val messageRepository = MessageRepository(application)

    //1 represents "true" in sqlite database.
    fun getAllMessagesOfUser(user: String) =
        messageRepository.getAllMessagesOfUser(user, 1)

    suspend fun sendMessage(message: Message) {
        messageRepository.sendMessageToUnknownUser(message)

    }
}