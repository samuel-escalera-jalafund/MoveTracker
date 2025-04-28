package com.example.movietracker.model

import com.example.movietracker.data.local.MovieEntity
import com.example.movietracker.data.remote.MovieDto

fun MovieDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        year = this.year,
        imageUrl = this.imageUrl,
        isFavorite = false,
        isWatched = false
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        year = this.year,
        imageUrl = this.imageUrl,
        isFavorite = this.isFavorite,
        isWatched = this.isWatched
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        year = this.year,
        imageUrl = this.imageUrl,
        isFavorite = this.isFavorite,
        isWatched = this.isWatched
    )
}

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        year = this.year,
        imageUrl = this.imageUrl,
        isFavorite = false,
        isWatched = false
    )
}

fun List<MovieEntity>.toDomainList(): List<Movie> = this.map { it.toDomain() }