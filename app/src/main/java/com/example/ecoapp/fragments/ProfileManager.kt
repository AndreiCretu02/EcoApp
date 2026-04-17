package com.example.ecoapp.data

import android.content.Context

object ProfileManager {

    private const val PREFS = "profile"
    private const val KEY_NAME = "username"

    fun saveName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_NAME, name).apply()
    }

    fun getName(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_NAME, "Eco Hero 🌱") ?: "Eco Hero 🌱"
    }
}