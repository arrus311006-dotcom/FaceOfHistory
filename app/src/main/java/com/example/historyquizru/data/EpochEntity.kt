package com.example.historyquizru.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "epochs")
data class EpochEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val orderIndex: Int
)