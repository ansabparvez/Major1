package com.devansab.begnn.data

import androidx.room.Embedded
import com.devansab.begnn.data.entities.Message

class LastMessage(@Embedded val message: Message, val name: String) {
}