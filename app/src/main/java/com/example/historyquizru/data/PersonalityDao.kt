package com.example.historyquizru.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PersonalityDao {
    @Query(
        "SELECT * FROM personalities " +
                "WHERE epochId = :epochId ORDER BY RANDOM() LIMIT 10"
    )
    suspend fun getRandomByEpoch(epochId: Int): List<PersonalityEntity>

    @Query(
        "SELECT * FROM personalities " +
                "WHERE epochId = :epochId AND id != :excludeId " +
                "ORDER BY RANDOM() LIMIT 3"
    )
    suspend fun getRandomWrong(
        epochId: Int,
        excludeId: Int
    ): List<PersonalityEntity>

    @Query("SELECT * FROM personalities WHERE epochId = :epochId ORDER BY name")
    suspend fun getByEpoch(epochId: Int): List<PersonalityEntity>

    @Query("SELECT * FROM personalities WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): PersonalityEntity?

    @Query("SELECT * FROM personalities WHERE epochId = :epochId")
    suspend fun getAllByEpoch(epochId: Int): List<PersonalityEntity>
}