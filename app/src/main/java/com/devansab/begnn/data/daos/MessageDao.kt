package com.devansab.begnn.data.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.devansab.begnn.data.entities.LastMessage
import com.devansab.begnn.data.entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages WHERE userName = :userName AND isAnonymous = :isAnonymous ORDER BY time")
    fun getAllMessagesOfUser(userName: String, isAnonymous: Int): Flow<List<Message>>

    @Query(
        "SELECT messages.*, name FROM users  JOIN (SELECT messages.* FROM messages WHERE isAnonymous = :isAnonymous ORDER BY time DESC) AS messages ON users.userName = messages.userName GROUP BY messages.userName"
    )
    fun getAllLastMessages(isAnonymous: Int): Flow<List<LastMessage>>
}