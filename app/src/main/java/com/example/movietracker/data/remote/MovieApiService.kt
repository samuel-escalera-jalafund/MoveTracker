package com.example.movietracker.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movies")
    suspend fun searchMovies(@Query("search") query: String): List<MovieDto>

    @GET("movies/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDto
}