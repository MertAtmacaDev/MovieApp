package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.api.RetrofitClient
import com.example.movieapp.model.Movie
import com.example.movieapp.storage.FavoritesManager
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var emptyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        emptyText = findViewById(R.id.emptyText)
        val recyclerView = findViewById<RecyclerView>(R.id.favoritesRecyclerView)

        adapter = MovieAdapter(emptyList()) { movie ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        val favoriteIds = FavoritesManager.getFavorites(this).toList()

        if (favoriteIds.isEmpty()) {
            adapter.updateMovies(emptyList())
            emptyText.visibility = View.VISIBLE
            return
        }
        emptyText.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val movies = mutableListOf<Movie>()
                for (id in favoriteIds) {
                    movies.add(RetrofitClient.api.getMovieDetail(id))
                }
                adapter.updateMovies(movies)
            } catch (e: Exception) {
                Toast.makeText(
                    this@FavoritesActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}