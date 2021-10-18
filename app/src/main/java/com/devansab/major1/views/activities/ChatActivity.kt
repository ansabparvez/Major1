package com.devansab.major1.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devansab.major1.R
import com.devansab.major1.adapters.ChatRVAdapter
import com.devansab.major1.data.entities.Chat

class ChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initViews()
    }

    private fun initViews() {
        rvChat = findViewById(R.id.rv_chat_chats)
        val chatList = ArrayList<Chat>()
        chatList.add(Chat("Hello!", System.currentTimeMillis() - 100000, Chat.TYPE_RECEIVED))
        chatList.add(Chat("How are you", System.currentTimeMillis() - 100000, Chat.TYPE_RECEIVED))
        chatList.add(Chat("I am good.", System.currentTimeMillis() - 90000, Chat.TYPE_SENT))
        chatList.add(Chat("What about you", System.currentTimeMillis() - 90000, Chat.TYPE_SENT))
        chatList.add(
            Chat(
                "I am also doing good.",
                System.currentTimeMillis() - 60000,
                Chat.TYPE_RECEIVED
            )
        )
        chatList.add(
            Chat(
                "So where are you now a days.",
                System.currentTimeMillis() - 60000,
                Chat.TYPE_RECEIVED
            )
        )
        chatList.add(Chat("I am at my home.", System.currentTimeMillis() - 50000, Chat.TYPE_SENT))
        chatList.add(Chat("WFH, haha", System.currentTimeMillis() - 50000, Chat.TYPE_SENT))
        chatList.add(Chat("That's great", System.currentTimeMillis() - 40000, Chat.TYPE_RECEIVED))
        chatList.add(
            Chat(
                "I need help with the code, actually I am new to kotlin and I can't seem to understand few things.",
                System.currentTimeMillis() - 40000, Chat.TYPE_RECEIVED
            )
        )
        chatList.add(
            Chat(
                "Can you help me?",
                System.currentTimeMillis() - 40000,
                Chat.TYPE_RECEIVED
            )
        )
        chatList.add(
            Chat(
                "haha kotlin is a funny language.",
                System.currentTimeMillis() - 30000,
                Chat.TYPE_SENT
            )
        )
        chatList.add(
            Chat(
                "Sure! I will help you, let me know what errors you are getting, I can help you in debugging.",
                System.currentTimeMillis() - 30000, Chat.TYPE_SENT
            )
        )


        val chatAdapter = ChatRVAdapter(chatList)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = chatAdapter
        chatAdapter.notifyDataSetChanged()

    }
}