package com.example.animething.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.animething.R
import com.example.animething.data.DisplayAnimeList
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

const val EXTRA_ANIME_INFO = "com.example.animething.DisplayAnimeList"

class AnimeDetailActivity : AppCompatActivity() {
    private var animeInfo: DisplayAnimeList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)

        if (intent != null && intent.hasExtra(EXTRA_ANIME_INFO)){
            animeInfo = intent.getSerializableExtra(EXTRA_ANIME_INFO) as DisplayAnimeList
            Picasso.get().load(animeInfo!!.images.jpg.image_url).into(findViewById<ImageView>(R.id.iv_anime_image))
            findViewById<TextView>(R.id.tv_anime_score).text = "Score: " + animeInfo!!.score.toString()
            findViewById<TextView>(R.id.tv_anime_genre).text = "Genre: " + animeInfo!!.genres[0].name.toString()
            findViewById<TextView>(R.id.tv_anime_episode).text = "Episode: " + animeInfo?.episodes.toString()
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