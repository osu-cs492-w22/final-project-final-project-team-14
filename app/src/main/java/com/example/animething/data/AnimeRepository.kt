package com.example.animething.data

import android.text.TextUtils
import com.example.animething.service.AnimeService
import com.example.animething.service.SearchService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class AnimeRepository(
    private val animeService: AnimeService,
    private val searchService: SearchService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    fun getAllAnimes() = animeService.getTopAnime()

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