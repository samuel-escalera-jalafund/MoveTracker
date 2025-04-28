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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val currentTab = MutableStateFlow(0)

    private val _movieDetails = MutableLiveData<Movie?>()
    val movieDetails: LiveData<Movie?> = _movieDetails

    private val _moviesToShow = MutableStateFlow<List<Movie>>(emptyList())
    val moviesToShow: StateFlow<List<Movie>> = _moviesToShow


    fun searchMovies(query: String) {
        viewModelScope.launch {
            if (currentTab.value == 0) {
                _moviesToShow.value = repository.searchMovies(query)
            }
        }
    }
    val favoriteMovies: Flow<List<Movie>> = repository.getFavoriteMovies()

    init {
        viewModelScope.launch {
            combine(
                repository.getFavoriteMovies(),
                repository.getWatchedMovies(),
                currentTab
            ) { favorites, watched, tab ->
                when (tab) {
                    1 -> favorites
                    2 -> watched
                    else -> emptyList()
                }
            }.collect { filteredMovies ->
                _moviesToShow.value = filteredMovies
            }
        }
    }

    fun showAllMovies() {
        currentTab.value = 0
    }

    fun showFavoriteMovies() {
        currentTab.value = 1
    }

    fun showWatchedMovies() {
        currentTab.value = 2
    }

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