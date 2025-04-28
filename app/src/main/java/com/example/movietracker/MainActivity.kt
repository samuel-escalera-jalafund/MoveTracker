package com.example.movietracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.example.movietracker.databinding.ActivityMainBinding
import com.example.movietracker.ui.DetailActivity
import com.example.movietracker.ui.MovieAdapter
import com.example.movietracker.ui.MovieViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
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

        setupTabs()
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupTabs() {
        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_all))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_favorites))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_watched))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> viewModel.showAllMovies()
                    1 -> viewModel.showFavoriteMovies()
                    2 -> viewModel.showWatchedMovies()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter(emptyList()) { movie ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("MOVIE_ID", movie.id)
            }
            startActivity(intent)
        }
        binding.moviesRecyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchMovies(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.takeIf { it.length > 2 }?.let { viewModel.searchMovies(it) }
                return true
            }
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.moviesToShow.collect { movies ->
                adapter.updateMovies(movies)
            }
        }
    }
}