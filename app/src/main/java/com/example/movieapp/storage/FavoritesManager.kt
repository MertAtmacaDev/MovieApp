package com.example.movieapp.storage

import android.content.Context

object FavoritesManager {

    private const val PREFS_NAME = "favorites_prefs"
    private const val KEY_FAVORITES = "favorite_ids"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getFavorites(context: Context): Set<Int> {
        val stored = prefs(context).getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        return stored.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun isFavorite(context: Context, movieId: Int): Boolean =
        getFavorites(context).contains(movieId)

    fun toggleFavorite(context: Context, movieId: Int): Boolean {
        val current = getFavorites(context).map { it.toString() }.toMutableSet()
        return if (current.contains(movieId.toString())) {
            current.remove(movieId.toString())
            prefs(context).edit().putStringSet(KEY_FAVORITES, current).apply()
            false  // artık favori değil
        } else {
            current.add(movieId.toString())
            prefs(context).edit().putStringSet(KEY_FAVORITES, current).apply()
            true   // artık favori
        }
    }
}