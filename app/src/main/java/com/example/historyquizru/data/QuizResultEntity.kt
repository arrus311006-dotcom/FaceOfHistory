package com.example.historyquizru.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quiz_results",
    indices = [Index(value = ["userId"]), Index(value = ["date"])]
)
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val epochId: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val date: Long
)