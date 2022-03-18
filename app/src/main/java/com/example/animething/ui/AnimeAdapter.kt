package com.example.animething.ui
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.animething.R
import com.example.animething.data.DisplayAnimeList
import com.example.animething.data.TopAnime
import com.squareup.picasso.Picasso

class AnimeAdapter(private val onAnimeClick: (DisplayAnimeList) -> Unit,
                   private val bookmarkStateManager: (ImageButton, DisplayAnimeList, Int) -> Unit,
                   private val bookmarksViewModel: AnimeBookmarkViewModel,
                   private val mainLifecycle: LifecycleOwner)
    : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {
    var animeList: List<DisplayAnimeList> = listOf()

    fun updateAnimeList(newAnimeList: List<DisplayAnimeList>) {
        animeList = newAnimeList.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = this.animeList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_item_layout,parent,false)
        return ViewHolder(view, onAnimeClick, bookmarkStateManager)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Catch and handle the entires shown on mainActivity to properly show their bookmark state
        bookmarksViewModel.animeBookmarks.observe(mainLifecycle) { bookmarks ->
            if (bookmarks != null) {
                bookmarkStateManager(holder.bookmarkButton, animeList[position], 1)
            }
        }

        holder.bind(this.animeList[position])
    }

    class ViewHolder(view: View,
                     val onAnimeClick: (DisplayAnimeList) -> Unit,
                     val bookmarkStateManager: (ImageButton, DisplayAnimeList, Int) -> Unit)
            : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val image = view.findViewById<ImageView>(R.id.image)

        private var currentAnime: DisplayAnimeList? = null

        var bookmarkButton = view.findViewById<ImageButton>(R.id.bookmark_button_main)
        init {
            view.setOnClickListener {
                currentAnime?.let(onAnimeClick)
            }
            //apply the bookmark button and the displayanimelist to mainActivity's bookmarkStateManager
            bookmarkButton.setOnClickListener {
                currentAnime?.let { it -> bookmarkStateManager(bookmarkButton, it, 0) }
            }
        }

        fun bind(anime: DisplayAnimeList) {
            currentAnime = anime

            name.text = anime.title
            // https://square.github.io/picasso/
            Picasso.get().load(anime.images.jpg.image_url).into(image)
        }
    }
}
