package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.media.data.db.converters.ListConverter
import com.example.playlistmaker.media.data.db.dao.FavoriteTrackDao
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.TrackDao
import com.example.playlistmaker.media.data.db.entities.FavoriteTrackEntity
import com.example.playlistmaker.media.data.db.entities.PlaylistEntity
import com.example.playlistmaker.media.data.db.entities.TrackEntity

@Database(
    entities = [FavoriteTrackEntity::class, PlaylistEntity::class, TrackEntity::class],
    version = 1
)
@TypeConverters(ListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTrackDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackDao(): TrackDao
}