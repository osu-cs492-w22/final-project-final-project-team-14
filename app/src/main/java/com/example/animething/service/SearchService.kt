package com.example.animething.service

import com.example.animething.data.DisplayAnimeList
import com.example.animething.data.SearchAnime
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("anime")
    fun getSearchedAnime(
        @Query("q") search: String
    ): SearchAnime

    companion object {
        private const val BASE_URL = "https://api.jikan.moe/v4/"
        fun create(): SearchService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(SearchService::class.java)
        }
    }
}