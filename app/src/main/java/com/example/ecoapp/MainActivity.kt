package com.example.ecoapp

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ecoapp.fragments.*
import org.osmdroid.util.GeoPoint
import android.location.*
import android.content.Context

class MainActivity : AppCompatActivity(), LocationListener {

    var lastLocation: GeoPoint? = null

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // 🔥 START GLOBAL LOCATION TRACKING
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                2f,
                this
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        if (savedInstanceState == null) {
            replaceFragment(MapFragment())
        }

        val nav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)

        nav.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.nav_map -> replaceFragment(MapFragment())
                R.id.nav_dashboard -> replaceFragment(DashboardFragment())
                R.id.nav_offers -> replaceFragment(OffersFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }

            true
        }
    }

    override fun onLocationChanged(location: Location) {
        lastLocation = GeoPoint(location.latitude, location.longitude)
    }

    fun getLastKnownLocation(): GeoPoint? = lastLocation

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}