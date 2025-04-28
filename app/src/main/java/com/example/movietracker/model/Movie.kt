package com.example.movietracker.model

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val imageUrl: String,
    val isFavorite: Boolean,
    val isWatched: Boolean
)