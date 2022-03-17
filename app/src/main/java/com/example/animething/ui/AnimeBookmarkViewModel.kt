package com.example.animething.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.animething.data.AnimeBookmarkDatabase
import com.example.animething.data.AnimeBookmarkRepository
import com.example.animething.data.DisplayAnimeList
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

//Class for activities to access and do stuff with the bookmark DB
class AnimeBookmarkViewModel(app: Application) : AndroidViewModel(app) {
    private val bookmarkRepo = AnimeBookmarkRepository(AnimeBookmarkDatabase.getDatabase(app).animeDao())
    val animeBookmarks = bookmarkRepo.getBookmarks().asLiveData()

    fun saveAnime(anime: DisplayAnimeList) {
        viewModelScope.launch {
            bookmarkRepo.addAnime(anime)
        }
    }

    fun deleteAnime(anime: DisplayAnimeList) {
        viewModelScope.launch {
            bookmarkRepo.removeAnime(anime)
        }
    }
}