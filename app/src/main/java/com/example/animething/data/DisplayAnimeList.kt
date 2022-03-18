package com.example.animething.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//Made into an entity for bookmarking
//genres are also stored in a MutableList<> instead of a List<> now
@Entity
data class DisplayAnimeList(
    val airing: Boolean,
    //val start_date: String,
    //val end_date: String,
    val episodes: Int?,
    //val image_url: String,
    val images: Images,
    val mal_id: Int,
    val members: Int,
    val rating: String?,
    val score: Double?,
    val synopsis: String?,
    @PrimaryKey val title: String,
    val type: String?,
    val url: String?,
    val status: String?,
    val genres: MutableList<Genres>
) : Serializable

