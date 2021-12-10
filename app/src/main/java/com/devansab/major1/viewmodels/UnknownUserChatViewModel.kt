package com.devansab.major1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devansab.major1.data.entities.LastMessage
import com.devansab.major1.data.entities.Message
import com.devansab.major1.data.repositories.LastMessageRepository
import com.devansab.major1.data.repositories.MessageRepository

class UnknownUserChatViewModel(application: Application) : AndroidViewModel(application) {
    private val messageRepository = MessageRepository(application)
    private val lastMessageRepository = LastMessageRepository(application)

    //1 represents "true" in sqlite database.
    fun getAllMessagesOfUser(user: String) =
        messageRepository.getAllMessagesOfUser(user, 1)

    suspend fun sendMessage(message: Message) {
        val lastMessage = LastMessage(
            message.userName, message.text, message.time,
            "Unknown", true
        )
        lastMessageRepository.updateLastMessage(lastMessage)
        messageRepository.sendMessageToUnknownUser(message)

    }
}