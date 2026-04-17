package com.example.ecoapp.data

object GlobalStats {

    var totalXP = 0
    var totalCO2Saved = 0.0
    var totalWaterSaved = 0.0
    var totalRescues = 0

    private var lastLevel = 1

    fun addRescue() {
        totalRescues++
    }

    fun addXP(xp: Int): Boolean {
        totalXP += xp

        val newLevel = getLevel()

        return if (newLevel > lastLevel) {
            lastLevel = newLevel
            true
        } else false
    }

    fun addCO2(co2: Double) {
        totalCO2Saved += co2
    }

    fun addWater(water: Double) {
        totalWaterSaved += water
    }

    fun getLevel(): Int {
        return totalXP / 100 + 1
    }
}