package com.example.animething.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animething.R
import com.example.animething.DisplayAnimeList
import com.squareup.picasso.Picasso

class AnimeAdapter (private val animeList: List<DisplayAnimeList>):
    RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    override fun getItemCount() = this.animeList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.anime_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.animeList[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val image = view.findViewById<ImageView>(R.id.image)

        fun bind(anime: DisplayAnimeList) {
            name.text = anime.title
            // https://square.github.io/picasso/
            Picasso.get().load(anime.image_url).into(image)
        }
    }
}
