package com.example.animething.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animething.R
import com.example.animething.data.DisplayAnimeList
import com.example.animething.data.TopAnime
import com.example.animething.databinding.ActivityMainBinding
import com.example.animething.service.AnimeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // View (binding) is used to interact with views and replaces findViewById
        binding.apply {
            val animeService = AnimeService.create()
            val call = animeService.getTopAnime()

            call.enqueue(object : Callback<TopAnime> {
                override fun onResponse(call: Call<TopAnime>, response: Response<TopAnime>) {
                    if (response.body() != null) {
                        val topAnimes = response.body()!!.data
                        // RecyclerView can be found in activtiy_main.xml
                        animeRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 3)
                        animeRecyclerView.setHasFixedSize(true)
                        animeRecyclerView.adapter = AnimeAdapter(topAnimes)
                        //animeRecyclerView.setAdapter(AnimeAdapter(topAnimes))
                    }
                }
                override fun onFailure(call: Call<TopAnime>, t: Throwable) {

                }
            })
        }
    }
}