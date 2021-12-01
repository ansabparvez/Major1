package com.devansab.major1.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.devansab.major1.data.entities.User

@Dao
abstract interface UserDao{

    @Insert(onConflict = REPLACE)
    fun insertUser(user: User)

}