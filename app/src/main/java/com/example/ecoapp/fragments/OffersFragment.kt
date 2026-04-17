package com.example.ecoapp.fragments

import android.os.*
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ecoapp.MainActivity
import com.example.ecoapp.R
import com.example.ecoapp.data.FoodManager
import org.osmdroid.util.GeoPoint

class OffersFragment : Fragment() {

    private lateinit var txtOffers: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        txtOffers = view.findViewById(R.id.txtOffers)
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

            val location = (activity as? MainActivity)?.getLastKnownLocation()

            if (location != null) {
                updateOffers(location)
            }

            handler.postDelayed(this, 1500)
        }
    }

    private fun updateOffers(userLocation: GeoPoint) {

        if (FoodManager.restaurants.isEmpty()) {
            txtOffers.text = "No restaurants nearby yet 🍽️"
            return
        }

        val sorted = FoodManager.restaurants.sortedBy {
            userLocation.distanceToAsDouble(it.location)
        }

        val text = StringBuilder()

        for (r in sorted) {

            val distance = userLocation.distanceToAsDouble(r.location)

            text.append("${r.name}\n")
            text.append("📍 ${distance.toInt()} m away\n")
            text.append("⭐ ${r.xp} XP\n\n")
        }

        txtOffers.text = text.toString()
    }
}