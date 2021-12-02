package com.devansab.major1.data.repositories

import android.app.Application
import com.devansab.major1.data.AppDatabase
import com.devansab.major1.data.daos.LastMessageDao
import com.devansab.major1.data.entities.LastMessage
import com.devansab.major1.data.entities.Message
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val application: Application) {

    private val appDatabase = AppDatabase.getInstance(application)
    private var lastMessageDao: LastMessageDao = appDatabase.lastMessageDao();
    private var messageDao = appDatabase.messageDao();

    fun getAllUnAnonymousLastMessages():
            Flow<List<LastMessage>> = lastMessageDao.getAllUnAnonymousLastMessages()

    fun getAllMessagesOfUser(username: String, isAnonymous: Int) :
            Flow<List<Message>> = messageDao.getAllMessagesOfUser(username, isAnonymous)

    suspend fun sendMessageAnonymously(message: Message){
        messageDao.insertMessage(message)
    }
}