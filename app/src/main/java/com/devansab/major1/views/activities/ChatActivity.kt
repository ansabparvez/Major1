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
import com.devansab.major1.viewmodels.ChatViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var viewmodel: ChatViewModel
    private lateinit var userName: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initViews()
    }

    private fun initViews() {
        rvChat = findViewById(R.id.rv_chat_chats)
        viewmodel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        )[ChatViewModel::class.java]

        findViewById<ImageButton>(R.id.ibtn_chat_sendMessage)
            .setOnClickListener { sendMessage() }

        userName = intent.getStringExtra("userName")!!
        name = intent.getStringExtra("name")!!

        viewmodel.viewModelScope.launch {
            viewmodel.getUserByUsername(userName).collect {
                findViewById<TextView>(R.id.tv_chat_userName)
                    .text = it.name
            }
        }

        viewmodel.viewModelScope.launch {
            viewmodel.getAllMessagesOfUser(userName, false).collect {
                Toasty.success(baseContext, "size: ${it.size}").show()
                DebugLog.i("ansab", "chat size: ${it.size}")
                displayChat(ArrayList(it))
            }
        }
    }

    private fun displayChat(messages: ArrayList<Message>) {
        rvChat.layoutManager = LinearLayoutManager(baseContext)
        val chatAdapter = ChatRVAdapter(messages)
        rvChat.adapter = chatAdapter
        chatAdapter.notifyDataSetChanged()
    }

    private fun sendMessage() {
        val etMessage = findViewById<EditText>(R.id.et_chat_message)
        val messageText = etMessage.text.toString().trim()
        if (messageText.isEmpty() || messageText.isBlank())
            return

        val message = Message(
            UUID.randomUUID().toString(),
            messageText, System.currentTimeMillis(), userName, sentByMe = true, isAnonymous = false
        )

        etMessage.setText("")
        viewmodel.viewModelScope.launch {
            viewmodel.sendMessageToKnownUser(message, userName, name)
        }
    }
}