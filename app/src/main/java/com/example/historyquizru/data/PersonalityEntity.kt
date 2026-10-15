package com.example.historyquizru.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "personalities",
    foreignKeys = [
        ForeignKey(
            entity = EpochEntity::class,
            parentColumns = ["id"],
            childColumns = ["epochId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["epochId"])]
)
data class PersonalityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val shortHint: String,
    val fullDescription: String,
    val yearsOfLife: String,
    val epochId: Int,
    val imageName: String
)