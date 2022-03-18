package com.example.animething.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animething.data.DisplayAnimeList
import com.example.animething.R
import com.squareup.picasso.Picasso

//Adapter for the bookmarks activity (recyclerview, mainly)
class AnimeBookmarksAdapter(private val onClick: (DisplayAnimeList) -> Unit)
    : RecyclerView.Adapter<AnimeBookmarksAdapter.ViewHolder>() {
    var bookmarkSet: List<DisplayAnimeList> = listOf()

    fun updateBookmarks(bookmarks: List<DisplayAnimeList>) {
//        Log.d("d", "------ Got data: " + bookmarks)
        bookmarkSet = bookmarks
        notifyDataSetChanged()
    }

    override fun getItemCount() = this.bookmarkSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmark_item_layout,parent,false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Log.d("d", "------ Handling data: " + bookmarkSet[position])
        holder.bind(bookmarkSet[position])
    }

    class ViewHolder(view: View, val onClick: (DisplayAnimeList) -> Unit)
        : RecyclerView.ViewHolder(view) {
        private var name: TextView? = view.findViewById(R.id.bookmark_name)
        private var image: ImageView? = view.findViewById(R.id.bookmark_image)

        private var currentAnime: DisplayAnimeList? = null

        init {
            view.setOnClickListener {
                currentAnime?.let(onClick)
            }
        }

        fun bind(bookmarkIn: DisplayAnimeList) {
            currentAnime = bookmarkIn

            if (name != null) {
                name!!.text = bookmarkIn.title
            }
            if (image != null) {
                // https://square.github.io/picasso/
                Picasso.get().load(bookmarkIn.images.jpg.image_url).into(image)
            }
        }
    }
}