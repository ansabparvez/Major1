package com.devansab.major1.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test")
data class Test(
    @PrimaryKey val id: Int,
    val testString: String) {
}