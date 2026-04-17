package com.example.ecoapp.fragments

import com.example.ecoapp.data.GlobalStats

object AchievementManager {

    data class Achievement(
        val title: String,
        val isUnlocked: Boolean
    )

    fun getAllAchievements(): List<Achievement> {

        return listOf(
            Achievement(
                "🔴 First 5 rescues",
                GlobalStats.totalRescues >= 5
            ),
            Achievement(
                "🔥 20 rescues",
                GlobalStats.totalRescues >= 20
            ),
            Achievement(
                "⭐ Reach Level 2",
                GlobalStats.totalXP >= 100
            ),
            Achievement(
                "💎 Reach Level 5",
                GlobalStats.totalXP >= 400
            )
        )
    }
}