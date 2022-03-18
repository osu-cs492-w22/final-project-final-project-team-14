package com.example.animething.data

import java.io.Serializable

data class DisplayAnimeList(
    val airing: Boolean,
    val episodes: String?,
    val images: Images,
    val mal_id: Int,
    val members: Int,
    val rating: String?,
    val score: String?,
    val synopsis: String?,
    val title: String?,
    val type: String?,
    val url: String?,
    val status: String?,
    val genres: List<Genres>
) : Serializable

