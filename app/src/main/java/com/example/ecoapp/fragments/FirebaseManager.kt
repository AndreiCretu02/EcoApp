package com.example.ecoapp.fragments

import com.google.firebase.firestore.FirebaseFirestore
import com.example.ecoapp.data.GlobalStats

object FirebaseManager {

    private val db = FirebaseFirestore.getInstance()

    fun saveStats(userId: String) {

        val data = hashMapOf(
            "totalRescues" to GlobalStats.totalRescues,
            "totalCO2Saved" to GlobalStats.totalCO2Saved,
            "totalWaterSaved" to GlobalStats.totalWaterSaved,
            "totalXP" to GlobalStats.totalXP
        )

        db.collection("users")
            .document(userId)
            .set(data)
    }

    fun loadStats(userId: String, onLoaded: () -> Unit = {}) {

        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                if (document != null && document.exists()) {

                    GlobalStats.totalRescues =
                        document.getLong("totalRescues")?.toInt() ?: 0

                    GlobalStats.totalCO2Saved =
                        document.getDouble("totalCO2Saved") ?: 0.0

                    GlobalStats.totalWaterSaved =
                        document.getDouble("totalWaterSaved") ?: 0.0

                    GlobalStats.totalXP =
                        document.getLong("totalXP")?.toInt() ?: 0
                }

                onLoaded()
            }
    }
}