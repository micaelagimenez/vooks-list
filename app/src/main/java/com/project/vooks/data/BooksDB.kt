package com.project.vooks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BooksData::class], version = 1, exportSchema = false)
abstract class BooksDB : RoomDatabase() {

    abstract fun booksDao(): BooksDao

    companion object {
        @Volatile
        private var INSTANCE: BooksDB? = null

        fun getDatabase(context: Context): BooksDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BooksDB::class.java,
                    "books_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}