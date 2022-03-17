package com.example.animething.data

//Repository for bookmarks
class AnimeBookmarkRepository(private val dao: DisplayAnimeDAO) {
    //add & remove functions, fairly standard
    suspend fun addAnime(newAnime: DisplayAnimeList) = dao.insert(newAnime)
    suspend fun removeAnime(oldAnime: DisplayAnimeList) = dao.delete(oldAnime)

    //get the full list of bookmarked animes
    fun getBookmarks() = dao.getAllBookmarks()
}