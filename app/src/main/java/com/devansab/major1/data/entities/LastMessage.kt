package com.devansab.major1.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lastMessages")
data class LastMessage(
    @PrimaryKey
    val userName: String,
    val text: String,
    val time: Long,
    val name: String
) {
}