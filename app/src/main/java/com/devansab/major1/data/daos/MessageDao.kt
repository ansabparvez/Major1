package com.devansab.major1.data.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.devansab.major1.data.entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
abstract interface MessageDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages WHERE userName = :userName AND isAnonymous = :isAnonymous ORDER BY time")
    fun getAllMessagesOfUser(userName: String, isAnonymous: Int): Flow<List<Message>>
}