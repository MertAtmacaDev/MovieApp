package com.example.movieapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.api.RetrofitClient
import com.example.movieapp.model.Movie
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.moviesRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        adapter = MovieAdapter(emptyList()) { movie ->
            val intent = android.content.Intent(this, DetailActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        findViewById<android.widget.Button>(R.id.searchScreenButton).setOnClickListener {
            startActivity(android.content.Intent(this, SearchActivity::class.java))
        }

        findViewById<android.widget.Button>(R.id.favoritesScreenButton).setOnClickListener {
            startActivity(android.content.Intent(this, FavoritesActivity::class.java))
        }

        loadPopularMovies()
    }

    private fun loadPopularMovies() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getPopularMovies()
                adapter.updateMovies(response.results)
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}