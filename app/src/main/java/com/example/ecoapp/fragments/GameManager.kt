package com.example.ecoapp.fragments

import org.osmdroid.util.GeoPoint

object GameManager {

    var currentLocation: GeoPoint? = null

    private val listeners = mutableListOf<() -> Unit>()

    fun updateLocation(point: GeoPoint) {
        currentLocation = point
        notifyListeners()
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it() }
    }
}