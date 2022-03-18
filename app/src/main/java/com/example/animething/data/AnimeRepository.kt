package com.example.animething.data

import com.example.animething.service.AnimeService
import com.example.animething.service.SearchService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepository(
    private val animeService: AnimeService,
    private val searchService: SearchService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    fun getAllAnimes() = animeService.getTopAnime()
    fun getRandom() = animeService.getRandomAnime()

    suspend fun searchedAnime(
        query: String
    ) : Result<List<DisplayAnimeList>> =
        withContext(ioDispatcher) {
            try {
                val results = animeService.getSearchedAnime(buildAnimeQuery(query))
                Result.success(results.searchedAnime)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun buildAnimeQuery(
        query: String
    ) : String {
        var fullQuery = query
//        if (!TextUtils.isEmpty(anime)) {
//            fullQuery += " anime:$anime"
//        }
        return fullQuery
    }
}