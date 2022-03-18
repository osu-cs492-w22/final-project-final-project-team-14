package com.example.animething.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.animething.R
import com.example.animething.data.AnimeRepository
import com.example.animething.data.DisplayAnimeList
import com.example.animething.service.AnimeService
import com.example.animething.service.SearchService
import com.squareup.picasso.Picasso

const val RANDOM_ANIME_INFO = "com.example.animething.DisplayAnimeList"

class RandomAnimeActivity  : AppCompatActivity() {
    private var animeInfo: DisplayAnimeList? = null

    lateinit var viewModel: AnimeViewModel
    val animeService = AnimeService.create()
    val searchService = SearchService.create()


    private val bookmarksViewModel: AnimeBookmarkViewModel by viewModels()
    private lateinit var bookmarkButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_anime_detail)

        viewModel = ViewModelProvider(this, AnimeViewModelFactory(AnimeRepository(animeService, searchService))).get(AnimeViewModel::class.java)
        viewModel.getRandomAnime()


        if (intent != null && intent.hasExtra(RANDOM_ANIME_INFO)){
            animeInfo = intent.getSerializableExtra(RANDOM_ANIME_INFO) as DisplayAnimeList
            findViewById<TextView>(R.id.tv_anime_title).text = animeInfo!!.title
            Picasso.get().load(animeInfo!!.images.jpg.image_url).into(findViewById<ImageView>(R.id.iv_anime_image))
            findViewById<TextView>(R.id.tv_anime_score).text = animeInfo!!.score.toString() + "/10"
            /*if (animeInfo!!.score?.compareTo(4) > 0) findViewById<TextView>(R.id.tv_anime_score).setTextColor(Color.RED)
            else if (animeInfo!!.score > 7) findViewById<TextView>(R.id.tv_anime_score).setTextColor(Color.GREEN)
            else findViewById<TextView>(R.id.tv_anime_score).setTextColor(Color.GRAY)*/
            if (animeInfo!!.genres.isNotEmpty()) findViewById<TextView>(R.id.tv_anime_genre).text = "Genre: " + animeInfo!!.genres[0].name
            findViewById<TextView>(R.id.tv_anime_episode).text = "Episodes: " + animeInfo!!.episodes.toString()
            findViewById<TextView>(R.id.tv_anime_status).text = "Status: " + animeInfo!!.status
            findViewById<TextView>(R.id.tv_anime_synopsis).text = animeInfo!!.synopsis

            //New stuff to handle bookmarks is here (catch changes in db, set default state correctly)
            bookmarkButton = findViewById(R.id.bookmark_button_detail)
            bookmarksViewModel.animeBookmarks.observe(this) { bookmarks ->
                if (bookmarks != null) {
                    if (bookmarksViewModel.animeBookmarks.value!!.any { it.title == animeInfo!!.title }) {
//                Log.d("d","--------------- found pair")
                        bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24_selected)
                    } else if (bookmarksViewModel.animeBookmarks.value!!.none { it.title == animeInfo!!.title }) {
                        bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    }
                }
            }

            //New stuff to handle bookmarks is here too (click listener)
            bookmarkButton.setOnClickListener {
                val buttonDrawable = bookmarkButton.drawable.constantState
                val internalBlankState = resources.getDrawable(R.drawable.ic_baseline_bookmark_24, null).constantState
                val internalSavedState = resources.getDrawable(R.drawable.ic_baseline_bookmark_24_selected, null).constantState
                if (buttonDrawable!!.equals(internalBlankState)) {
//                Log.d("d", "Save")
                    bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24_selected)
                    bookmarksViewModel.saveAnime(animeInfo!!)
                }
                else if (buttonDrawable!!.equals(internalSavedState)) {
//                Log.d("d", "Delete")
                    bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    bookmarksViewModel.deleteAnime(animeInfo!!)
                }
            }
        }
        val newRandomBtn: Button = findViewById(R.id.btn_new_random)
        newRandomBtn.setOnClickListener {
            viewModel.getRandomAnime()
            bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24)
            animeInfo = viewModel.randomAnime.value
            findViewById<TextView>(R.id.tv_anime_title).text = animeInfo!!.title
            Picasso.get().load(animeInfo!!.images.jpg.image_url).into(findViewById<ImageView>(R.id.iv_anime_image))
            findViewById<TextView>(R.id.tv_anime_score).text = animeInfo!!.score.toString() + "/10"
            /*if (animeInfo!!.score < 4) findViewById<TextView>(R.id.tv_anime_score).setTextColor(Color.RED)
            else if (animeInfo!!.score > 7) findViewById<TextView>(R.id.tv_anime_score).setTextColor(Color.GREEN)
            else findViewById<TextView>(R.id.tv_anime_score).setTextColor(Color.GRAY)*/
            if (animeInfo!!.genres.isNotEmpty()) findViewById<TextView>(R.id.tv_anime_genre).text = "Genre: " + animeInfo!!.genres[0].name
            findViewById<TextView>(R.id.tv_anime_episode).text = "Episodes: " + animeInfo!!.episodes.toString()
            findViewById<TextView>(R.id.tv_anime_status).text = "Status: " + animeInfo!!.status
            findViewById<TextView>(R.id.tv_anime_synopsis).text = animeInfo!!.synopsis
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_anime_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_anime -> {
                viewAnimeOnMAL()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun viewAnimeOnMAL() {
        val url = animeInfo!!.url
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}