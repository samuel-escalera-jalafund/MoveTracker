package com.example.movietracker.ui

import android.os.Bundle
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
    }

    private fun observeViewModel() {
        viewModel.movieDetails.observe(this) { movie ->
            movie?.let { bindMovieDetails(it) }
        }
    }

    private fun bindMovieDetails(movie: Movie) {
        binding.movieTitle.text = movie.title
        binding.movieYear.text = movie.year.toString()

        Glide.with(this)
            .load(movie.imageUrl)
            .into(binding.moviePoster)
    }
}