package com.example.animething.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
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

    //bookmarks viewmodel and nav-to button
    private val bookmarksViewModel: AnimeBookmarkViewModel by viewModels()
    private lateinit var bookmarkNavButton: Button

    private lateinit var randomAnimeNavButton: Button

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: AnimeViewModel
    val animeService = AnimeService.create()
//  If you're wondering why the adapter isn't here, bookmark functionality currently works
//    by passing bookmarksViewModel into the adapter, and calling that isn't allowed before
//    onCreate. Basically, the adapter has moved down into onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, AnimeViewModelFactory(AnimeRepository(animeService))).get(AnimeViewModel::class.java)
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

        //adapter was moved here for bookmarks to work
        val adapter = AnimeAdapter(::onAnimeClick,
            ::bookmarkStateManager,
            bookmarksViewModel,
            this@MainActivity)

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
        viewModel.getRandomAnime()
    }

    private fun onAnimeClick(anime: DisplayAnimeList){
        val intent = Intent(this, AnimeDetailActivity::class.java).apply {
            putExtra(EXTRA_ANIME_INFO, anime)
        }
        startActivity(intent)
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_random -> {
                randomAnime()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun randomAnime(){
        viewModel.getRandomAnime()
        val intent = Intent(this, RandomAnimeActivity::class.java).apply {
            putExtra(RANDOM_ANIME_INFO, viewModel.randomAnime.value)
        }
        startActivity(intent)
    }*/

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