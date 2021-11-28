package com.devansab.major1.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.devansab.major1.data.entities.LastMessage

@Dao
abstract interface LastMessageDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertLastMessage(lastMessage: LastMessage);
}