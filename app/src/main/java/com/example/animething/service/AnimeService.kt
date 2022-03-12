package com.example.animething.service


import com.example.animething.TopAnime
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeService {
    @GET("top/anime")
    // Call is a RetroFit2 function that sends a request and returns a response
    fun getTopAnime(): Call<TopAnime>
    
    companion object {
        val BASE_URL = "https://api.jikan.moe/v3/"
        fun create(): AnimeService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(AnimeService::class.java)
        }
    }
}