package com.example.ecoapp.fragments

import android.app.AlertDialog
import android.os.*
import android.view.*
import androidx.fragment.app.Fragment
import com.example.ecoapp.data.FoodManager
import com.example.ecoapp.data.GlobalStats
import com.google.firebase.auth.FirebaseAuth
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.*

class MapFragment : Fragment() {

    private lateinit var map: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    private lateinit var fogOverlay: org.osmdroid.views.overlay.Overlay
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        Configuration.getInstance().load(
            requireContext(),
            requireContext().getSharedPreferences("osmdroid", 0)
        )
        Configuration.getInstance().userAgentValue = requireContext().packageName

        map = MapView(requireContext())
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        map.controller.setZoom(18.0)
        map.controller.setCenter(GeoPoint(44.43225, 26.10626))

        // ✅ LOCATION
        val provider = GpsMyLocationProvider(requireContext())
        locationOverlay = MyLocationNewOverlay(provider, map)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()

        map.overlays.add(locationOverlay)

        // ✅ FOG INIT (ONLY ONCE)
       // FogOverlay.load(requireContext())
        if (!map.overlays.contains(FogOverlay)) {
            map.overlays.add(FogOverlay)
        }

        locationOverlay.runOnFirstFix {
            activity?.runOnUiThread {
                val loc = locationOverlay.myLocation
                if (loc != null) {
                    map.controller.setCenter(loc)
                    map.controller.setZoom(18.0)
                }
            }
        }

        return map
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }

    private val updateRunnable = object : Runnable {
        override fun run() {

            val loc = locationOverlay.myLocation

            if (loc != null) {

                // 🔥 DEBUG (IMPORTANT)
                println("LOCATION: ${loc.latitude}, ${loc.longitude}")

                // ✅ ALWAYS ADD (NO FILTER)
                FogOverlay.addRevealedArea(loc, map)

                // ✅ FORCE REDRAW
                map.invalidate()

                // ✅ SAVE
                //FogOverlay.save(requireContext())

                // ✅ SPAWN FOOD
                val r = FoodManager.trySpawn(loc)
                if (r != null) addRestaurantMarker(r)
            }

            handler.postDelayed(this, 1500)
        }
    }

    private fun addRestaurantMarker(r: FoodManager.Restaurant) {

        val marker = Marker(map)
        marker.position = r.location
        marker.title = r.name

        marker.setOnMarkerClickListener { m, _ ->

            AlertDialog.Builder(requireContext())
                .setTitle(r.name)
                .setMessage(
                    "Collect food?\n\n" +
                            "+${r.xp} XP\n" +
                            "+${"%.2f".format(r.co2)} kg CO₂\n" +
                            "+${"%.0f".format(r.water)} L water"
                )
                .setPositiveButton("Collect") { _, _ ->

                    GlobalStats.addRescue()
                    val levelUp = GlobalStats.addXP(r.xp)
                    GlobalStats.addCO2(r.co2)
                    GlobalStats.addWater(r.water)

                    map.overlays.remove(m)
                    FoodManager.restaurants.remove(r)

                    map.invalidate()

                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) FirebaseManager.saveStats(userId)

                    if (levelUp) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("🎉 Level Up!")
                            .setMessage("Level ${GlobalStats.getLevel()}")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()

            true
        }

        map.overlays.add(marker)
    }
}