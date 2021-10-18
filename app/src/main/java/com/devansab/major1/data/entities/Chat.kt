package com.devansab.major1.data.entities

data class Chat(val message: String, val time: Long, val type: Int) {
    companion object {
        val TYPE_SENT: Int = 1;
        val TYPE_RECEIVED: Int = 2;
    }
}