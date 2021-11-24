package com.devansab.major1.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.devansab.major1.data.entities.Test

@Dao
interface TestDao {

    @Query("SELECT * FROM test")
    suspend fun getAll(): List<Test>

    @Insert
    suspend fun insert(test: Test)

    @Delete
    suspend fun delete(test: Test)
}