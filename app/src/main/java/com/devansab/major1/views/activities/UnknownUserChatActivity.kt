package com.devansab.major1.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devansab.major1.R
import com.devansab.major1.adapters.ChatRVAdapter
import com.devansab.major1.data.entities.Message
import com.devansab.major1.utils.DebugLog
import com.devansab.major1.viewmodels.UnknownUserChatViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class UnknownUserChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var viewmodel: UnknownUserChatViewModel
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unknown_user_chat)

        initViews()
    }

    private fun initViews() {
        rvChat = findViewById(R.id.rv_unknownChat_chats)
        viewmodel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        )[UnknownUserChatViewModel::class.java]

        findViewById<ImageButton>(R.id.ibtn_unknownChat_sendMessage)
            .setOnClickListener { sendMessage() }

        userName = intent.getStringExtra("userName")!!

        viewmodel.viewModelScope.launch {
            viewmodel.getAllMessagesOfUser(userName).collect {
                DebugLog.i("ansab", "chat size: ${it.size}")
                displayChat(ArrayList(it))
            }
        }
    }

    private fun displayChat(list: ArrayList<Message>) {
        rvChat.layoutManager = LinearLayoutManager(baseContext)
        val adapter = ChatRVAdapter(list)
        rvChat.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun sendMessage() {
        val etMessage = findViewById<EditText>(R.id.et_unknownChat_message)
        val messageText = etMessage.text.toString().trim()
        if (messageText.isEmpty() || messageText.isBlank())
            return

        val message = Message(
            UUID.randomUUID().toString(),
            messageText, System.currentTimeMillis(), userName, sentByMe = true, isAnonymous = true
        )

        etMessage.setText("")
        viewmodel.viewModelScope.launch {
            viewmodel.sendMessage(message)
        }
    }
}