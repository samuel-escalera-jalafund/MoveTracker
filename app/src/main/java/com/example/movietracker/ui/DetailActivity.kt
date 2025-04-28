package com.example.movietracker.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movietracker.databinding.ActivityDetailBinding
import com.example.movietracker.model.Movie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: MovieViewModel by viewModels()
    private var currentMovie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId != -1) {
            viewModel.getMovieDetails(movieId)
            observeViewModel()
        } else {
            finish()
        }

        binding.favoriteButton.setOnClickListener {
            currentMovie?.let { movie ->
                viewModel.toggleFavorite(movie)
                currentMovie = movie.copy(isFavorite = !movie.isFavorite)
                updateFavoriteButton()
                val message = if (currentMovie?.isFavorite == true) {
                    "Película añadida a favoritos"
                } else {
                    "Película removida de favoritos"
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.movieDetails.observe(this) { movie ->
            movie?.let {
                currentMovie = it
                bindMovieDetails(it)
                updateFavoriteButton()
            }
        }
    }

    private fun bindMovieDetails(movie: Movie) {
        binding.movieTitle.text = movie.title
        binding.movieYear.text = movie.year.toString()

        Glide.with(this)
            .load(movie.imageUrl)
            .into(binding.moviePoster)
    }

    private fun updateFavoriteButton() {
        currentMovie?.let { movie ->
            val buttonText = if (movie.isFavorite) {
                "Quitar de favoritos"
            } else {
                "Añadir a favoritos"
            }
            binding.favoriteButton.text = buttonText
        }
    }
}