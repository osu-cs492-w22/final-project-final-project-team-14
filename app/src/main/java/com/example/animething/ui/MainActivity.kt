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
import android.widget.ImageButton
import androidx.activity.viewModels
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
import com.example.animething.data.TopAnime
import com.example.animething.databinding.ActivityMainBinding
import com.example.animething.service.AnimeService
import com.example.animething.service.SearchService
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

//Reference: The ViewModel Architecture implementation is based on an online tutorial source
//Source: https://howtodoandroid.com/mvvm-retrofit-recyclerview-kotlin/


class MainActivity : AppCompatActivity() {

    //bookmarks viewmodel and nav-to button
    private val bookmarksViewModel: AnimeBookmarkViewModel by viewModels()
    private lateinit var bookmarkNavButton: Button

    private lateinit var randomAnimeNavButton: Button

    private lateinit var binding: ActivityMainBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var searchResultsListRV: RecyclerView
    lateinit var viewModel: AnimeViewModel
    val animeService = AnimeService.create()
    val searchService = SearchService.create()
//  If you're wondering why the adapter isn't here, bookmark functionality currently works
//    by passing bookmarksViewModel into the adapter, and calling that isn't allowed before
//    onCreate. Basically, the adapter has moved down into onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, AnimeViewModelFactory(AnimeRepository(animeService, searchService))).get(AnimeViewModel::class.java)
        //binding.animeRecyclerView.adapter = adapter

        // Random Anime Button Listener
        randomAnimeNavButton = findViewById(R.id.random_nav_button)
        randomAnimeNavButton.setOnClickListener {
            viewModel.getRandomAnime()
            val intent = Intent(this, RandomAnimeActivity::class.java).apply {
                putExtra(RANDOM_ANIME_INFO, viewModel.randomAnime.value)
            }
            startActivity(intent)
        }

        //test bookmark navigation button. Change this to work with the bottom nav bar when it's made!
        bookmarkNavButton = findViewById(R.id.bookmark_nav_button)
        bookmarkNavButton.setOnClickListener {
            val intent = Intent(this, AnimeBookmarksActivity::class.java)
            startActivity(intent)
        }

        requestQueue = Volley.newRequestQueue(this)

        searchResultsListRV = findViewById(R.id.animeRecyclerView)
        searchResultsListRV.layoutManager = GridLayoutManager(this@MainActivity, 3)
        searchResultsListRV.setHasFixedSize(true)

        //adapter was moved here for bookmarks to work
        val adapter = AnimeAdapter(
            ::onAnimeClick,
            ::bookmarkStateManager,
            bookmarksViewModel,
            this@MainActivity)

        viewModel.animes.observe(this, Observer {

            adapter.updateAnimeList(it)
        })
        viewModel.getAnimes()
        viewModel.getRandomAnime()

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
                doAnimeSearch(query, order.toString(), adapter)
            }
        }
    }

    fun doAnimeSearch(q: String, order: String, adapter: AnimeAdapter) {

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

        this.setTitle("Results for \"" + q + "\"");
    }

    override fun onResume() {
        Log.d("MainActivity", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("MainActivity", "onPause")
        super.onPause()
    }

    private fun onAnimeClick(anime: DisplayAnimeList){
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

    // >>> MANAGE BOOKMARKING/BOOKMARK STATE ON MAINACTIVITY <<<
    // can be called in two states
    // state 0 is intended to catch the bookmarking being saved/deleted (clicked on)
    // state 1 is intended to display the correct state of the bookmark to the *scrolling* user

    //This code doesn't double check with the database before taking actions, but that should be fine for the database,
    //  and will work better for the sake of not overloading the API I guess?
    private fun bookmarkStateManager(buttonClicked: ImageButton, animeSelected: DisplayAnimeList, state: Int) {
        //state for clicking on the bookmark button
        if (state == 0) {
//        Log.d("d", "Bookmark clicked for " + animeSelected.title)
            val buttonDrawable = buttonClicked.drawable.constantState
            val internalBlankState = resources.getDrawable(R.drawable.ic_baseline_bookmark_24, null).constantState
            val internalSavedState = resources.getDrawable(R.drawable.ic_baseline_bookmark_24_selected, null).constantState
            if (buttonDrawable!!.equals(internalBlankState)) {
//                Log.d("d", "Save")
                buttonClicked.setImageResource(R.drawable.ic_baseline_bookmark_24_selected)
                bookmarksViewModel.saveAnime(animeSelected)
            }
            else if (buttonDrawable!!.equals(internalSavedState)) {
//                Log.d("d", "Delete")
                buttonClicked.setImageResource(R.drawable.ic_baseline_bookmark_24)
                bookmarksViewModel.deleteAnime(animeSelected)
            }
        }
        //state for scrolling up/down the grid list
        else if (state == 1) {
            if (bookmarksViewModel.animeBookmarks.value!!.any{ it.title == animeSelected.title }) {
//                Log.d("d","--------------- found pair")
                buttonClicked.setImageResource(R.drawable.ic_baseline_bookmark_24_selected)
            }
            else if (bookmarksViewModel.animeBookmarks.value!!.none{ it.title == animeSelected.title }) {
                buttonClicked.setImageResource(R.drawable.ic_baseline_bookmark_24)
            }
        }
    }
}