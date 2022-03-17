package com.example.animething.data

import androidx.room.TypeConverter

//Converter functions for all the custom types in DisplayAnimeList (or for anywhere, I guess)
class ClassConverters {
    @TypeConverter
    fun imgFromUrl(valIn: Urls?): Images? {
        return valIn?.let { Images(it) }
    }

    @TypeConverter
    fun imgToUrl(valIn: Images?): Urls? {
        return valIn?.jpg
    }

    @TypeConverter
    fun urlFromStr(valIn: String?): Urls? {
        return valIn?.let { Urls(valIn) }
    }

    @TypeConverter
    fun urlToStr(valIn: Urls?): String? {
        return valIn?.image_url
    }

    @TypeConverter
    fun genreFromStr(valIn: String?): MutableList<Genres>? {
        return valIn?.split(", ")?.map { Genres(valIn) }?.toMutableList()
    }

    @TypeConverter
    fun genreToStr(valIn: MutableList<Genres>?): String? {
        return valIn?.map { it.name }?.joinToString(separator = ", ")
    }
}