package com.example.animething.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//DB class for managing bookmarks
@Database(entities = [DisplayAnimeList::class], version = 1)
@TypeConverters(ClassConverters::class)
abstract class AnimeBookmarkDatabase : RoomDatabase() {
    abstract fun animeDao(): DisplayAnimeDAO

    companion object {
        @Volatile private var databaseObj: AnimeBookmarkDatabase? = null

        fun getDatabase(context: Context): AnimeBookmarkDatabase {
            return databaseObj ?: synchronized(this) {
                databaseObj ?: buildDatabase(context).also {
                    databaseObj = it
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AnimeBookmarkDatabase::class.java, "BookmarkedAnime").build()
    }
}