package com.devansab.begnn.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.devansab.begnn.data.entities.LastMessage
import kotlinx.coroutines.flow.Flow

@Dao
abstract interface LastMessageDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertLastMessage(lastMessage: LastMessage)

    @Query("SELECT * FROM lastMessages")
    fun getAll(): Flow<List<LastMessage>>

    @Query("SELECT * FROM lastMessages WHERE isAnonymous = :isAnonymous")
    fun getAllLastMessages(isAnonymous : Int): Flow<List<LastMessage>>
}