package com.example.animething.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animething.R
import com.example.animething.data.DisplayAnimeList

//Bookmark page activity
class AnimeBookmarksActivity : AppCompatActivity() {
    private val bookmarksViewModel: AnimeBookmarkViewModel by viewModels()

    private lateinit var bookmarkAdapter: AnimeBookmarksAdapter
    private lateinit var bookmarkListRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_bookmarks)

        bookmarkListRV = findViewById(R.id.bookmark_recycler_view)
        bookmarkAdapter = AnimeBookmarksAdapter(::onBookmarkClick)

        bookmarkListRV.layoutManager = LinearLayoutManager(this)
        bookmarkListRV.setHasFixedSize(true)
        bookmarkListRV.adapter = bookmarkAdapter

        bookmarksViewModel.animeBookmarks.observe(this) { bookmarks ->
            if (bookmarks != null) {
//                Log.d("d", "------ Sending data: " + bookmarks)
                bookmarkAdapter.updateBookmarks(bookmarks)
            }
        }
    }

    fun onBookmarkClick(anime: DisplayAnimeList) {
        val intent = Intent(this, AnimeDetailActivity::class.java).apply {
            putExtra(EXTRA_ANIME_INFO, anime)
        }
        startActivity(intent)
    }
}