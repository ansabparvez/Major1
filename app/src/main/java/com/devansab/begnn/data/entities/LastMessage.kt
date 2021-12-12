package com.devansab.begnn.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lastMessages")
data class LastMessage(
    @PrimaryKey
    //This is the user name is of the person who is another side of the chat.
    //Event the message is sent by me, user name will be of other user.
    val userName: String,
    val text: String,
    val time: Long,
    val name: String,
    val isAnonymous: Boolean
) {
}