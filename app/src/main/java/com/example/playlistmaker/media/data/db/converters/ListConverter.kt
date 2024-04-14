package com.example.playlistmaker.media.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson

@ProvidedTypeConverter
class ListConverter(private val gson: Gson) {

    @TypeConverter
    fun listToString(list: List<Long>): String = gson.toJson(list)

    @TypeConverter
    fun stringToList(str: String): List<Long> = gson.fromJson(str, Array<Long>::class.java).toList()
}