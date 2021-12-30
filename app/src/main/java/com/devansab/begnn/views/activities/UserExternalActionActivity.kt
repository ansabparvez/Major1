package com.devansab.begnn.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import com.devansab.begnn.R
import com.devansab.begnn.utils.DebugLog

class UserExternalActionActivity : AppCompatActivity() {

    companion object {
        const val TYPE = "type"
        const val TYPE_NEW_MESSAGE_NOTIFICATION = "newMessageNotification"
        const val NEW_MESSAGE_USER_NAME = "newMessageUserName"
        const val NEW_MESSAGE_PERSON_TYPE = "newMessagePersonType"
        const val NEW_MESSAGE_PERSON_NAME = "newMessagePersonName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_external_action)

        init()
    }

    private fun init() {
        val type = intent.getStringExtra(TYPE)
        if (type == null || type.isEmpty())
            return

        if (type == TYPE_NEW_MESSAGE_NOTIFICATION) {
            handleNewMessage()
        }
    }

    private fun handleNewMessage() {
        val name = intent.getStringExtra(NEW_MESSAGE_PERSON_NAME)
        val userName = intent.getStringExtra(NEW_MESSAGE_USER_NAME)
        val isAnonymous = intent.getBooleanExtra(NEW_MESSAGE_PERSON_TYPE, true)

        val messageIntent: Intent = if (isAnonymous) {
            Intent(this, UnknownUserChatActivity::class.java).apply {
                putExtra("userName", userName)
                putExtra("name", name)
            }
        } else {
            Intent(this, KnownUserChatActivity::class.java).apply {
                putExtra("userName", userName)
                putExtra("name", name)
            }
        }
        startActivity(messageIntent)
        finish()
    }
}