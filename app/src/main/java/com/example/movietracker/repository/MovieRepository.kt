package com.example.movietracker.repository

import com.example.movietracker.data.local.MovieDao
import com.example.movietracker.data.remote.MovieApiService
import com.example.movietracker.model.toDomain
import com.example.movietracker.model.toDomainList
import com.example.movietracker.model.Movie
import com.example.movietracker.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) {
    suspend fun searchMovies(query: String): List<Movie> {
        return apiService.searchMovies(query).map { it.toEntity().toDomain() }
    }

    fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { it.toDomainList() }
    }

    suspend fun toggleFavorite(movie: Movie) {
        movieDao.insertMovie(
            movie.toEntity().copy(isFavorite = !movie.isFavorite)
        )
    }

    suspend fun getMovieDetails(movieId: Int): Movie {
        return apiService.getMovieDetails(movieId).toDomain()
    }
}