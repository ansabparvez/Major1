package com.devansab.major1.data.repositories

import android.app.Application
import com.devansab.major1.data.AppDatabase
import com.devansab.major1.data.daos.LastMessageDao
import com.devansab.major1.data.entities.LastMessage
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val application: Application) {

    private lateinit var lastMessageDao: LastMessageDao;
    private val appDatabase = AppDatabase.getInstance(application)

    init {
        lastMessageDao = appDatabase.lastMessageDao()
    }

    fun getAllUnAnonymousLastMessages() :
            Flow<List<LastMessage>> = lastMessageDao.getAllUnAnonymousLastMessages()


}