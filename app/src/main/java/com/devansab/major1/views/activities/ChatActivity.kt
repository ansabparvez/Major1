package com.devansab.major1.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.devansab.major1.R
import com.devansab.major1.viewmodels.ChatViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var viewmodel: ChatViewModel

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

        val userName = intent.getStringExtra("userName");

        viewmodel.viewModelScope.launch {
            viewmodel.getUserByUsername(userName!!).collect {
                findViewById<TextView>(R.id.tv_chat_userName)
                    .text = it.name
            }
        }


    }
}