package com.example.animething.data

import java.io.Serializable

data class SearchAnime(
    val searchedAnime: List<DisplayAnimeList>
) : Serializable

