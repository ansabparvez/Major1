package com.devansab.begnn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devansab.begnn.data.entities.LastMessage
import com.devansab.begnn.data.repositories.MessageRepository
import kotlinx.coroutines.flow.Flow

class UnknownChatLastMsgsViewModel(application: Application) : AndroidViewModel(application) {

    private val messageRepository = MessageRepository(application)

    fun getAllAnonymousLastMessages():
    //Anonymous, so provide 1 for room query
            Flow<List<LastMessage>> = messageRepository.getAllLastMessages(1)

}