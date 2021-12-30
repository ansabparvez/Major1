package com.devansab.begnn.data.entities

import androidx.room.Embedded

class LastMessage(@Embedded val message: Message, val name: String)