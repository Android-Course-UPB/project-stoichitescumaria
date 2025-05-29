package com.example.favoritebooksapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [FavoriteBookEntity::class], version = 1, exportSchema = false)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun favoriteBookDao(): FavoriteBookDao

    companion object {
        @Volatile
        private var Instance: BooksDatabase? = null

        fun getDatabase(context: Context): BooksDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BooksDatabase::class.java, "user_database")
                    .build().also { Instance = it }
            }
        }
    }
}


