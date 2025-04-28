package com.example.movietracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.model.Movie
import com.bumptech.glide.Glide
import com.example.movietracker.R

class MovieAdapter(
    private val movies: List<Movie>,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.movieTitle)
        private val yearTextView: TextView = itemView.findViewById(R.id.movieYear)
        private val posterImageView: ImageView = itemView.findViewById(R.id.moviePoster)

        fun bind(movie: Movie) {
            titleTextView.text = movie.title
            yearTextView.text = movie.year.toString()

            Glide.with(itemView.context)
                .load(movie.imageUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(posterImageView)

            itemView.setOnClickListener { onItemClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size
}