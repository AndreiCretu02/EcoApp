package com.example.ecoapp.fragments

import android.content.Context
import org.osmdroid.util.GeoPoint
import org.json.JSONArray
import org.json.JSONObject

object FogStorage {

    private const val PREFS = "fog_data"
    private const val KEY = "cleared_areas"

    fun save(context: Context, points: List<GeoPoint>) {

        val array = JSONArray()

        points.forEach {
            val obj = JSONObject()
            obj.put("lat", it.latitude)
            obj.put("lon", it.longitude)
            array.put(obj)
        }

        context.getSharedPreferences(PREFS, 0)
            .edit()
            .putString(KEY, array.toString())
            .apply()
    }

    fun load(context: Context): List<GeoPoint> {

        val json = context.getSharedPreferences(PREFS, 0)
            .getString(KEY, null) ?: return emptyList()

        val array = JSONArray(json)
        val list = mutableListOf<GeoPoint>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                GeoPoint(
                    obj.getDouble("lat"),
                    obj.getDouble("lon")
                )
            )
        }

        return list
    }
}