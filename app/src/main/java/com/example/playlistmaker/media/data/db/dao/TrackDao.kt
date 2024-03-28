package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entities.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: TrackEntity)

    @Delete
    suspend fun delete(track: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY addedTime DESC")
    suspend fun getAll(): List<TrackEntity>

    @Query("SELECT COUNT(trackId) FROM track_table WHERE trackId = :trackId")
    suspend fun countById(trackId: String): Int

    suspend fun isTrackExist(trackId: String): Boolean {
        return countById(trackId) > 0
    }
}