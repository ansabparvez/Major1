package com.devansab.major1.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userName: String,
    val name: String,
    val isAnonymous: Boolean
) {

}