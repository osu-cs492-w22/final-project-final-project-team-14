package com.example.animething.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.animething.R
import com.example.animething.data.AnimeRepository
import com.example.animething.data.DisplayAnimeList
import com.example.animething.databinding.ActivityMainBinding
import com.example.animething.service.AnimeService

//Reference: The ViewModel Architecture implementation is based on an online tutorial source
//Source: https://howtodoandroid.com/mvvm-retrofit-recyclerview-kotlin/


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: AnimeViewModel
    val animeService = AnimeService.create()
    val adapter = AnimeAdapter(::onAnimeClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, AnimeViewModelFactory(AnimeRepository(animeService))).get(AnimeViewModel::class.java)
        //binding.animeRecyclerView.adapter = adapter
        // View (binding) is used to interact with views and replaces findViewById
        binding.apply {
            //val animeService = AnimeService.create()
            //val call = animeService.getTopAnime()
            //viewModel.getAnimes()

            animeRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 3)
            animeRecyclerView.setHasFixedSize(true)
            animeRecyclerView.adapter = adapter


            /*call.enqueue(object : Callback<TopAnime> {
                override fun onResponse(call: Call<TopAnime>, response: Response<TopAnime>) {
                    if (response.body() != null) {
                        val topAnimes = response.body()!!.data
                        // RecyclerView can be found in activtiy_main.xml
                        animeRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 3)
                        animeRecyclerView.setHasFixedSize(true)
                        animeRecyclerView.adapter = AnimeAdapter(::onAnimeClick, topAnimes)

                        //animeRecyclerView.setAdapter(AnimeAdapter(topAnimes))
                    }
                }
                override fun onFailure(call: Call<TopAnime>, t: Throwable) {

                }
            })*/
        }
        viewModel.animes.observe(this, Observer {

            adapter.updateAnimeList(it)
        })

        viewModel.getAnimes()
    }

    private fun onAnimeClick(anime: DisplayAnimeList){
        val intent = Intent(this, AnimeDetailActivity::class.java).apply {
            putExtra(EXTRA_ANIME_INFO, anime)
        }
        startActivity(intent)
    }
}