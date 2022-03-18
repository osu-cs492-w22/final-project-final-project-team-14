package com.example.animething.service


import com.example.animething.data.RandomAnime
import com.example.animething.data.SearchAnime
import com.example.animething.data.TopAnime
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeService {
    @GET("top/anime")
    // Call is a RetroFit2 function that sends a request and returns a response
    fun getTopAnime(): Call<TopAnime>

    @GET("random/anime")
    fun getRandomAnime(): Call<RandomAnime>

    @GET("anime")
    fun getSearchedAnime(
        @Query("q") search: String
    ): SearchAnime
    
    companion object {
        val BASE_URL = "https://api.jikan.moe/v4/"
        fun create(): AnimeService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(AnimeService::class.java)
        }
    }
}