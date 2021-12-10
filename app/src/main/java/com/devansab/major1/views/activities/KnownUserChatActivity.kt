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
import com.devansab.major1.viewmodels.KnownUserChatViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class KnownUserChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var viewmodel: KnownUserChatViewModel
    private lateinit var userName: String
    private lateinit var name: String
    private var populated = false
    private lateinit var adapter: ChatRVAdapter;
    private val chatList = ArrayList<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_known_user_chat)

        initViews()
    }

    private fun initViews() {
        rvChat = findViewById(R.id.rv_chat_chats)
        viewmodel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        )[KnownUserChatViewModel::class.java]

        findViewById<ImageButton>(R.id.ibtn_chat_sendMessage)
            .setOnClickListener { sendMessage() }

        userName = intent.getStringExtra("userName")!!
        name = intent.getStringExtra("name")!!
        findViewById<TextView>(R.id.tv_chat_userName)
            .text = name

        rvChat.layoutManager = LinearLayoutManager(baseContext)
        adapter = ChatRVAdapter(chatList)
        rvChat.adapter = adapter

        viewmodel.viewModelScope.launch {
            viewmodel.getAllMessagesOfUser(userName, false).collect {
                DebugLog.i("ansab", "chat size: ${it.size}")
                displayChat(it)
            }
        }
    }

    private fun displayChat(list: List<Message>) {
        if (list.isEmpty())
            return

        val prePos = chatList.size
        val itemCount = list.size - prePos
        if (!populated) {
            chatList.addAll(list)
            populated = true
        } else
            chatList.add(list[list.size - 1])
        adapter.notifyItemRangeInserted(prePos, itemCount)
        rvChat.scrollToPosition(chatList.size - 1)
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
            viewmodel.sendMessage(message, name)
        }
    }
}