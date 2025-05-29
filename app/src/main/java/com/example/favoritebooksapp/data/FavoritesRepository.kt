package com.example.favoritebooksapp.data

class FavoritesRepository(private val dao: FavoriteBookDao) {

    suspend fun addFavorite(id: String) {
        val cleanId = id.trim()
        require(cleanId.isNotEmpty()) { "Book ID cannot be empty" }
        dao.insertFavorite(FavoriteBookEntity(cleanId))
    }

    suspend fun removeFavorite(id: String) {
        val cleanId = id.trim()
        require(cleanId.isNotEmpty()) { "Book ID cannot be empty" }
        dao.deleteFavorite(cleanId)
    }

    suspend fun isFavorite(id: String): Boolean {
        val cleanId = id.trim()
        require(cleanId.isNotEmpty()) { "Book ID cannot be empty" }
        return dao.isFavorite(cleanId)
    }

    suspend fun getAllFavoriteIds(): List<String> = dao.getAllFavoriteBookIds()
}
