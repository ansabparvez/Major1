package com.devansab.begnn.services

import com.devansab.begnn.data.entities.LastMessage
import com.devansab.begnn.data.entities.Message
import com.devansab.begnn.data.repositories.LastMessageRepository
import com.devansab.begnn.data.repositories.MessageRepository
import com.devansab.begnn.data.repositories.UserRepository
import com.devansab.begnn.utils.DebugLog
import com.devansab.begnn.utils.SharedPrefManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        DebugLog.i(this, "fcm token: " + token)
        super.onNewToken(token)
        SharedPrefManager.getInstance(baseContext).setFCMToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        DebugLog.i(this, "fcm message: " + remoteMessage.data.toString())
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data;
        if (data.containsKey("messageId")) {
            parseMessage(data)
        }
    }

    private fun parseMessage(messageData: Map<String, String>) {
        val messageId = messageData["messageId"]!!
        val text = messageData["text"]!!
        val time = messageData["time"]!!.toLong()
        val userName = messageData["userName"]!!
        val isAnonymous = messageData["isAnonymous"].equals("true")

        val messageRepository = MessageRepository(application)
        val lastMessageRepository = LastMessageRepository(application)
        val userRepository = UserRepository(application)
        val message = Message(messageId, text, time, userName, false, isAnonymous);
        GlobalScope.launch {
            messageRepository.insertMessage(message);
            if (!isAnonymous) {
                userRepository.getUserByUsername(userName)
                    .collect {
                        val lastMessage = LastMessage(userName, text, time, it.name, false)
                        lastMessageRepository.updateLastMessage(lastMessage)
                    }
            } else {
                val lastMessage = LastMessage(userName, text, time, "Anonymous", true)
                lastMessageRepository.updateLastMessage(lastMessage)
            }

        }
    }
}