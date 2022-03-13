package com.example.animething.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.animething.R
import com.example.animething.data.DisplayAnimeList
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
            findViewById<TextView>(R.id.tv_anime_genre).text = "Genre: " + animeInfo!!.genres[0].name
            findViewById<TextView>(R.id.tv_anime_episode).text = "Episode: " + animeInfo!!.episodes.toString()
            findViewById<TextView>(R.id.tv_anime_status).text = "Status: " + animeInfo!!.status
            findViewById<TextView>(R.id.tv_anime_synopsis).text = animeInfo!!.synopsis
        }
    }


}