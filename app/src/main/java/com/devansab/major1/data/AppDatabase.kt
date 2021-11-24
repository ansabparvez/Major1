package com.devansab.major1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devansab.major1.data.daos.TestDao
import com.devansab.major1.data.entities.Test

@Database(entities = [Test::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun testDao(): TestDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "major1_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}