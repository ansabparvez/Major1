package com.devansab.major1.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.devansab.major1.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract interface UserDao{

    @Insert(onConflict = REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE userName = :userName")
    fun getUserByUsername(userName: String) : Flow<User>

}