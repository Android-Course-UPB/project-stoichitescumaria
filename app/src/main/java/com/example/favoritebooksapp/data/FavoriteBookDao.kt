package com.example.favoritebooksapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteBookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(book: FavoriteBookEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE bookId = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Query("DELETE FROM favorite_books WHERE bookId = :id")
    suspend fun deleteFavorite(id: String)

    @Query("SELECT bookId FROM favorite_books")
    suspend fun getAllFavoriteBookIds(): List<String>
}
