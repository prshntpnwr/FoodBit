package com.example.foodBit.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Categories::class,
        Restaurant::class,
        Photo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

}