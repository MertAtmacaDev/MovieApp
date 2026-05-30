package com.example.movieapp.api

import com.example.movieapp.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): com.example.movieapp.model.Movie

    @GET("movie/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieResponse
}