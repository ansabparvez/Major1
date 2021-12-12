package com.devansab.begnn.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devansab.begnn.R
import com.devansab.begnn.adapters.ChatRVAdapter
import com.devansab.begnn.data.entities.Message
import com.devansab.begnn.utils.DebugLog
import com.devansab.begnn.viewmodels.UnknownUserChatViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class UnknownUserChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var viewmodel: UnknownUserChatViewModel
    private lateinit var userName: String
    private var populated = false
    private lateinit var adapter: ChatRVAdapter;
    private val chatList = ArrayList<Message>()

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

        rvChat.layoutManager = LinearLayoutManager(baseContext)
        adapter = ChatRVAdapter(chatList)
        rvChat.adapter = adapter

        viewmodel.viewModelScope.launch {
            viewmodel.getAllMessagesOfUser(userName).collect {
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