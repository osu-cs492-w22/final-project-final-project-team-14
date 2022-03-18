package com.example.animething.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.animething.R
import com.example.animething.data.AnimeRepository
import com.example.animething.data.DisplayAnimeList
import com.example.animething.data.SearchAnime
import com.example.animething.data.TopAnime
import com.example.animething.databinding.ActivityMainBinding
import com.example.animething.service.AnimeService
import com.example.animething.service.SearchService
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Reference: The ViewModel Architecture implementation is based on an online tutorial source
//Source: https://howtodoandroid.com/mvvm-retrofit-recyclerview-kotlin/

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var searchResultsListRV: RecyclerView
    lateinit var viewModel: AnimeViewModel
    val animeService = AnimeService.create()
    val searchService = SearchService.create()
    val adapter = AnimeAdapter(::onAnimeClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)

        searchResultsListRV = findViewById(R.id.animeRecyclerView)
        searchResultsListRV.layoutManager = GridLayoutManager(this@MainActivity, 3)
        searchResultsListRV.setHasFixedSize(true)

        viewModel = ViewModelProvider(this, AnimeViewModelFactory(AnimeRepository(animeService, searchService))).get(AnimeViewModel::class.java)

        viewModel.animes.observe(this, Observer {
            adapter.updateAnimeList(it)
        })
        viewModel.getAnimes()

        // View (binding) is used to interact with views and replaces findViewById
        binding.apply {
            animeRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 3)
            animeRecyclerView.setHasFixedSize(true)
            animeRecyclerView.adapter = adapter
        }

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val searchBoxes: EditText = findViewById(R.id.search_anime_input)
        val searchBtn: Button = findViewById(R.id.btnSearch)
        searchBtn.setOnClickListener {
            val query = searchBoxes.text.toString()
            if (!TextUtils.isEmpty(query)) {
                val order = sharedPrefs.getString(
                    getString(R.string.pref_order_by_key),
                    null
                )
                doRepoSearch(query, order.toString())
            }
        }
    }


    fun doRepoSearch(q: String, order: String) {
        Log.d("What is order?", println(order).toString())
        val url = "https://api.jikan.moe/v4/anime?q=$q&order_by=$order"

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<TopAnime> =
            moshi.adapter(TopAnime::class.java)

        val req = StringRequest(
            Request.Method.GET,
            url,
            {
                var results = jsonAdapter.fromJson(it)
                Log.d("MainActivity", results.toString())
                adapter.updateAnimeList(results?.data)
                searchResultsListRV.visibility = View.VISIBLE
            },
            {
                Log.d("MainActivity", "Error fetching from $url: ${it.message}")
            }
        )

        searchResultsListRV.visibility = View.INVISIBLE
        requestQueue.add(req)
    }

    override fun onResume() {
        Log.d("MainActivity", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("MainActivity", "onPause")
        super.onPause()
    }


    private fun onAnimeClick(anime: DisplayAnimeList) {
        val intent = Intent(this, AnimeDetailActivity::class.java).apply {
            putExtra(EXTRA_ANIME_INFO, anime)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}