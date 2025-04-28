package com.example.movietracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietracker.model.Movie
import com.example.movietracker.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.movietracker.model.toDomain

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _movieDetails = MutableLiveData<Movie?>()
    val movieDetails: LiveData<Movie?> = _movieDetails

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _searchResults.value = repository.searchMovies(query)
        }
    }

    val favoriteMovies: Flow<List<Movie>> = repository.getFavoriteMovies()

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val apiMovie = repository.getMovieDetails(movieId)
                val localMovie = repository.getMovieById(movieId)

                val combinedMovie = apiMovie.copy(
                    isFavorite = localMovie?.isFavorite ?: false
                )

                _movieDetails.value = combinedMovie
            } catch (e: Exception) {
                _movieDetails.value = null
            }
        }
    }

    suspend fun getMovieById(movieId: Int): Movie? {
        return repository.getMovieById(movieId)?.toDomain()
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            repository.toggleFavorite(movie)
            _movieDetails.value = movie.copy(isFavorite = !movie.isFavorite)
        }
    }

    fun toggleWatched(movie: Movie) {
        viewModelScope.launch {
            repository.toggleWatched(movie)
            _movieDetails.value = movie.copy(isWatched = !movie.isWatched)
        }
    }

}