package com.example.movieapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.api.RetrofitClient
import kotlinx.coroutines.launch
import android.widget.Button
import com.example.movieapp.storage.FavoritesManager

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movieId = intent.getIntExtra("MOVIE_ID", -1)

        val poster = findViewById<ImageView>(R.id.detailPoster)
        val title = findViewById<TextView>(R.id.detailTitle)
        val rating = findViewById<RatingBar>(R.id.detailRating)
        val overview = findViewById<TextView>(R.id.detailOverview)
        val similarRecyclerView = findViewById<RecyclerView>(R.id.similarRecyclerView)

        similarRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        if (movieId == -1) {
            Toast.makeText(this, "Movie not found", Toast.LENGTH_SHORT).show()
            return
        }

        val favoriteButton = findViewById<Button>(R.id.favoriteButton)

        fun updateFavoriteButton() {
            favoriteButton.text =
                if (FavoritesManager.isFavorite(this, movieId)) "★ In Favorites"
                else "☆ Add to Favorites"
        }
        updateFavoriteButton()

        favoriteButton.setOnClickListener {
            val nowFavorite = FavoritesManager.toggleFavorite(this, movieId)
            updateFavoriteButton()
            Toast.makeText(
                this,
                if (nowFavorite) "Added to favorites" else "Removed from favorites",
                Toast.LENGTH_SHORT
            ).show()
        }

        lifecycleScope.launch {
            try {
                val movie = RetrofitClient.api.getMovieDetail(movieId)
                title.text = movie.title
                overview.text = movie.overview
                rating.rating = ((movie.voteAverage ?: 0.0) / 2).toFloat()

                Glide.with(this@DetailActivity)
                    .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                    .into(poster)

                val similar = RetrofitClient.api.getSimilarMovies(movieId)
                val similarAdapter = MovieAdapter(similar.results, useSmallLayout = true) { clicked ->
                    val intent = android.content.Intent(this@DetailActivity, DetailActivity::class.java)
                    intent.putExtra("MOVIE_ID", clicked.id)
                    startActivity(intent)
                }
                similarRecyclerView.adapter = similarAdapter

            } catch (e: Exception) {
                Toast.makeText(
                    this@DetailActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}