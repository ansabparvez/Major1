package com.devansab.major1.data.daos

import androidx.room.*
import com.devansab.major1.data.entities.Test

@Dao
interface TestDao {

    @Query("SELECT * FROM test")
    fun getAll(): List<Test>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(test: Test)

    @Delete
    suspend fun delete(test: Test)
}