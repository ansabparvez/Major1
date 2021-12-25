package com.devansab.begnn.services

import com.devansab.begnn.data.entities.Message
import com.devansab.begnn.data.repositories.MessageRepository
import com.devansab.begnn.data.repositories.UserRepository
import com.devansab.begnn.utils.DebugLog
import com.devansab.begnn.utils.MyNotificationManager
import com.devansab.begnn.utils.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        DebugLog.i(this, "on new fcm token: $token")
        super.onNewToken(token)
        if (FirebaseAuth.getInstance().currentUser == null)
            return
        SharedPrefManager.getInstance(baseContext).setFCMToken(token)
        UserRepository(application).updateFcmToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        DebugLog.i(this, "fcm message: " + remoteMessage.data.toString())
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        if (data.containsKey("messageId")) {
            parseMessage(data)
        }
    }

    private fun parseMessage(messageData: Map<String, String>) {
        if (FirebaseAuth.getInstance().currentUser == null)
            return
        val messageId = messageData["messageId"]!!
        val text = messageData["text"]!!
        val time = messageData["time"]!!.toLong()
        val userName = messageData["userName"]!!
        val isAnonymous = messageData["isAnonymous"].equals("true")

        val messageRepository = MessageRepository(application)
        val userRepository = UserRepository(application)
        val message = Message(messageId, text, time, userName, false, isAnonymous)
        GlobalScope.launch(Dispatchers.IO) {
            messageRepository.insertMessage(message)
            userRepository.getUserByUsername(message.userName)
            val user = userRepository.getUserByUsername(userName)

            if (user == null && isAnonymous) {
                userRepository.fetchAndStoreAnonymousUserData(userName)
                MyNotificationManager.showMessageNotification(message, "Anonymous")
            } else {
                MyNotificationManager.showMessageNotification(message, user!!.name)
            }
        }
    }
}