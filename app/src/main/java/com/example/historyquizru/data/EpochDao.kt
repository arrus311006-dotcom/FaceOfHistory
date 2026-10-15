package com.example.historyquizru.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface EpochDao {
    @Query("SELECT * FROM epochs ORDER BY orderIndex")
    suspend fun getAll(): List<EpochEntity>

    @Query("SELECT * FROM epochs WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): EpochEntity?
}