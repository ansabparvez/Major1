package com.devansab.major1.services

import com.devansab.major1.utils.DebugLog
import com.devansab.major1.utils.SharedPrefManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        DebugLog.i(this, "fcm token: "+token)
        super.onNewToken(token)
        SharedPrefManager.getInstance(baseContext).setFCMToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        DebugLog.i(this, "fcm message: "+remoteMessage.data.toString())
        super.onMessageReceived(remoteMessage)
    }
}