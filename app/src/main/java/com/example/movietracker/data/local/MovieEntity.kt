package com.example.movietracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: Int,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false
)