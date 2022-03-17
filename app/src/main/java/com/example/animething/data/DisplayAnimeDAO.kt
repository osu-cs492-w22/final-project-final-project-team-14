package com.example.animething.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//DAO for the bookmarked anime
@Dao
interface DisplayAnimeDAO {
    @Insert(onConflict = REPLACE)
    suspend fun insert(animeDetails: DisplayAnimeList)

    @Delete
    suspend fun delete(animeDetails: DisplayAnimeList)

    @Query("SELECT * FROM DisplayAnimeList")
    fun getAllBookmarks(): Flow<List<DisplayAnimeList>>
}