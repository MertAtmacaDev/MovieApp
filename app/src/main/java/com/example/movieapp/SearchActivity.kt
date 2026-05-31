package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.api.RetrofitClient
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchInput = findViewById<EditText>(R.id.searchInput)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val recyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)

        adapter = MovieAdapter(emptyList()) { movie ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.searchMovies(query)
                adapter.updateMovies(response.results)
                if (response.results.isEmpty()) {
                    Toast.makeText(this@SearchActivity, "No results", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@SearchActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}