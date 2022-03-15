package com.example.animething.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animething.data.AnimeRepository
import com.example.animething.data.DisplayAnimeList
import com.example.animething.data.TopAnime
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {
    val animes = MutableLiveData<List<DisplayAnimeList>>()

    fun getAnimes() {
        val response = repository.getAllAnimes()
        response.enqueue(object : retrofit2.Callback<TopAnime> {
            override fun onResponse(call: Call<TopAnime>, response: Response<TopAnime>) {

                animes.postValue(response.body()!!.data)

            }

            override fun onFailure(call: Call<TopAnime>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}


