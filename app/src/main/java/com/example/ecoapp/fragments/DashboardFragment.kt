package com.example.ecoapp.fragments

import android.os.*
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ecoapp.R
import com.example.ecoapp.data.GlobalStats

class DashboardFragment : Fragment() {

    private lateinit var txtLevel: TextView
    private lateinit var txtMeals: TextView
    private lateinit var txtCO2: TextView
    private lateinit var txtWater: TextView
    private lateinit var txtAchievements: TextView

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        txtLevel = view.findViewById(R.id.txtLevel)
        txtMeals = view.findViewById(R.id.txtMeals)
        txtCO2 = view.findViewById(R.id.txtCO2)
        txtWater = view.findViewById(R.id.txtWater)
        txtAchievements = view.findViewById(R.id.txtAchievements)
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
            updateUI()
            handler.postDelayed(this, 1500)
        }
    }

    private fun updateUI() {

        txtLevel.text = "Level: ${GlobalStats.getLevel()}"
        txtMeals.text = "Meals: ${GlobalStats.totalRescues}"
        txtCO2.text = "CO₂: ${"%.2f".format(GlobalStats.totalCO2Saved)} kg"
        txtWater.text = "Water: ${"%.0f".format(GlobalStats.totalWaterSaved)} L"

    }
}