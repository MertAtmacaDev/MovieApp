package com.example.movieapp.model

data class MovieResponse(
    val page: Int,
    val results: List<Movie>
)