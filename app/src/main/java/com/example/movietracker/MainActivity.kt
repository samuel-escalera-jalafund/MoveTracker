package com.example.movietracker

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.movietracker.databinding.ActivityMainBinding
import com.example.movietracker.ui.MovieAdapter
import com.example.movietracker.ui.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter(emptyList()) { movie ->
            Toast.makeText(this, "Selected: ${movie.title}", Toast.LENGTH_SHORT).show()
        }
        binding.moviesRecyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchMovies(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Implementación opcional para búsqueda en tiempo real
                newText?.let { viewModel.searchMovies(it) }
                return false
            }
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.searchResults.observe(this@MainActivity) { movies ->
                adapter = MovieAdapter(movies) { movie ->
                }
                binding.moviesRecyclerView.adapter = adapter
            }

            viewModel.favoriteMovies.collectLatest { favorites ->
            }
        }
    }
}