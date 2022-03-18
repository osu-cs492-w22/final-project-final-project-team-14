package com.example.animething.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.animething.R
import com.example.animething.data.DisplayAnimeList
import com.squareup.picasso.Picasso

class SearchAdapter (
    private val onAnimeClick: List<DisplayAnimeList>
): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = this.onAnimeClick.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = onAnimeClick[position]
        val view = holder.itemView

        val name = view.findViewById<TextView>(R.id.name)
        val image = view.findViewById<ImageView>(R.id.image)

        name.text = anime.title
        Picasso.get().load(anime.images.jpg.image_url).into(image)
    }

}
