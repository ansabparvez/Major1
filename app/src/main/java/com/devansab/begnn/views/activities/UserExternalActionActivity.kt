package com.devansab.begnn.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devansab.begnn.R

class UserExternalActionActivity : AppCompatActivity() {

    companion object {
        const val TYPE = "type"
        const val TYPE_NEW_MESSAGE_NOTIFICATION = "newMessageNotification"
        const val NEW_MESSAGE_USER_NAME = "newMessageUserName";
        const val NEW_MESSAGE_PERSON_TYPE = "newMessagePersonType";
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

    }
}