package com.example.historyquizru.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuizResultDao {
    @Insert
    suspend fun insert(result: QuizResultEntity): Long

    @Query("SELECT * FROM quiz_results WHERE userId = :userId ORDER BY date DESC")
    suspend fun getByUser(userId: Long): List<QuizResultEntity>
}