package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entities.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: FavoriteTrackEntity)

    @Delete
    suspend fun delete(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY addedTime DESC")
    suspend fun getAll(): List<FavoriteTrackEntity>

    @Query("SELECT COUNT(trackId) FROM favorite_tracks WHERE trackId = :trackId")
    suspend fun countById(trackId: String): Int

    suspend fun isTrackExist(trackId: String): Boolean {
        return countById(trackId) > 0
    }
}