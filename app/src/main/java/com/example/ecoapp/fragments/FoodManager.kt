package com.example.ecoapp.data

import org.osmdroid.util.GeoPoint
import kotlin.math.sqrt
import kotlin.random.Random

object FoodManager {

    data class Restaurant(
        val location: GeoPoint,
        val name: String,
        val xp: Int,
        val co2: Double,
        val water: Double
    )

    val restaurants = mutableListOf<Restaurant>()

    private var lastSpawnLocation: GeoPoint? = null

    fun trySpawn(center: GeoPoint): Restaurant? {

        val last = lastSpawnLocation

        // 🔥 Only spawn every 50 meters
        if (last != null && distanceMeters(last, center) < 50) return null

        val offsetLat = (Random.nextDouble() - 0.5) / 500
        val offsetLon = (Random.nextDouble() - 0.5) / 500

        val names = listOf(
            "Pizza 🍕", "Sushi 🍣", "Vegan 🥗", "Burger 🍔"
        )

        val restaurant = Restaurant(
            location = GeoPoint(center.latitude + offsetLat, center.longitude + offsetLon),
            name = names.random(),
            xp = Random.nextInt(5, 15),
            co2 = Random.nextDouble(0.1, 0.5),
            water = Random.nextDouble(20.0, 100.0) // ✅ FIXED
        )

        restaurants.add(restaurant)
        lastSpawnLocation = center

        return restaurant
    }

    private fun distanceMeters(a: GeoPoint, b: GeoPoint): Double {
        val dx = a.latitude - b.latitude
        val dy = a.longitude - b.longitude
        return sqrt(dx * dx + dy * dy) * 111000
    }
}