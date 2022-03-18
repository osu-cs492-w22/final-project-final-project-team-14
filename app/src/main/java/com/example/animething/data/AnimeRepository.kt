package com.example.animething.data

import com.example.animething.service.AnimeService

class AnimeRepository(private val animeService: AnimeService){
    fun getAllAnimes() = animeService.getTopAnime()
    fun getRandom() = animeService.getRandomAnime()
}