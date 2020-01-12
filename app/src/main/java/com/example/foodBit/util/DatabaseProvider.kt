package com.example.foodBit.util

import android.app.Application
import androidx.room.Room
import com.example.foodBit.database.AppDao
import com.example.foodBit.database.AppDatabase
import javax.inject.Singleton

/**
 * a singleton class that provide data access object
 */
@Singleton
class DatabaseProvider {

    fun getAppDao(application: Application): AppDao {
        val database = getDatabase(application)
        return database.appDao()
    }

    fun getDatabase(application: Application): AppDatabase {
        return if (db != null) {
            db!!
        } else {
            val db: AppDatabase = Room.databaseBuilder(application, AppDatabase::class.java, "restaurantApp_db")
                .fallbackToDestructiveMigration()
                .build()
            setDatabase(db)
            db
        }
    }

    private var db: AppDatabase? = null
    private fun setDatabase(db: AppDatabase) {
        this.db = db
    }

    companion object {
        internal var dbProvider: DatabaseProvider? = null

        val instance: DatabaseProvider
            get() {
                if (dbProvider == null) {
                    dbProvider = DatabaseProvider()
                }
                return dbProvider!!
            }
    }
}