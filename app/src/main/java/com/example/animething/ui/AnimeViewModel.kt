package com.example.animething.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animething.data.AnimeRepository
import com.example.animething.data.DisplayAnimeList
import com.example.animething.data.SearchAnime
import com.example.animething.data.TopAnime
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

enum class LoadingStatus {
    LOADING, ERROR, SUCCESS
}

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {
    val animes = MutableLiveData<List<DisplayAnimeList>>()

    fun getAnimes() {
        val response = repository.getAllAnimes()
        response.enqueue(object : retrofit2.Callback<TopAnime> {
            override fun onResponse(call: Call<TopAnime>, response: Response<TopAnime>) {
                animes.postValue(response.body()!!.data)
            }
            override fun onFailure(call: Call<TopAnime>, t: Throwable) {

            }
        })
    }

    private val _searchResults = MutableLiveData<List<DisplayAnimeList>?>(null)
    val searchResults: LiveData<List<DisplayAnimeList>?> = _searchResults

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadSearchResults(
        query: String
    ){
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.searchedAnime(query)
            _searchResults.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false ->LoadingStatus.ERROR
            }
        }
    }
}


