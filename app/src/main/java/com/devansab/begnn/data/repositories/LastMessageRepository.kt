package com.devansab.begnn.data.repositories

import android.app.Application
import com.devansab.begnn.data.AppDatabase
import com.devansab.begnn.data.entities.LastMessage
import kotlinx.coroutines.flow.Flow

class LastMessageRepository(private val application: Application) {

    private val appDatabase = AppDatabase.getInstance(application)
    private val lastMessageDao = appDatabase.lastMessageDao()

    fun getAllLastMessages(isAnonymous: Int):
            Flow<List<LastMessage>> = lastMessageDao.getAllLastMessages(isAnonymous)

    suspend fun updateLastMessage(lastMessage: LastMessage) {
        lastMessageDao.insertLastMessage(lastMessage)
    }
}