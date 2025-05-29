package com.example.favoritebooksapp

import com.example.favoritebooksapp.data.FavoriteBookEntity
import com.example.favoritebooksapp.data.FavoriteBookDao
import com.example.favoritebooksapp.data.FavoritesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FakeFavoriteBookDao : FavoriteBookDao {
    private val favoriteIds = mutableSetOf<String>()

    override suspend fun insertFavorite(book: FavoriteBookEntity) {
        favoriteIds.add(book.bookId)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return favoriteIds.contains(id)
    }

    override suspend fun deleteFavorite(id: String) {
        favoriteIds.remove(id)
    }

    override suspend fun getAllFavoriteBookIds(): List<String> {
        return favoriteIds.toList()
    }
}

class FavoritesRepositoryMockTest {

    private lateinit var dao: FakeFavoriteBookDao
    private lateinit var repository: FavoritesRepository

    @Before
    fun setup() {
        dao = FakeFavoriteBookDao()
        repository = FavoritesRepository(dao)
    }

    @Test
    fun addFavorite_and_isFavorite_work() = runBlocking {
        repository.addFavorite("book1")
        assertTrue(repository.isFavorite("book1"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun addFavorite_emptyId_throws() = runBlocking {
        repository.addFavorite("  ")
    }

    @Test
    fun removeFavorite_removesCorrectly() = runBlocking {
        repository.addFavorite("book2")
        assertTrue(repository.isFavorite("book2"))
        repository.removeFavorite("book2")
        assertFalse(repository.isFavorite("book2"))
    }

    @Test
    fun getAllFavoriteIds_returnsAll() = runBlocking {
        repository.addFavorite("book3")
        repository.addFavorite("book4")
        val all = repository.getAllFavoriteIds()
        assertTrue(all.containsAll(listOf("book3", "book4")))
    }
}
