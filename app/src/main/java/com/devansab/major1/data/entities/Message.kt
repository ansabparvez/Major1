package com.devansab.major1.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    val messageId: String,
    val text: Long,
    val time: String,
    val userName: String
) {
}