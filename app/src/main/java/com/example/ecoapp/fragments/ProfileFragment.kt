package com.example.ecoapp.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.ecoapp.LoginActivity
import com.example.ecoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnChangeName = view.findViewById<Button>(R.id.btnChangeName)
        val txtUser = view.findViewById<TextView>(R.id.txtUser)
        val container = view.findViewById<LinearLayout>(R.id.achievementsContainer)

        val user = FirebaseAuth.getInstance().currentUser
        txtUser.text = user?.displayName ?: user?.email ?: "Guest"

        // ✅ LOGOUT
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        // ✅ CHANGE NAME
        btnChangeName.setOnClickListener {

            val input = EditText(requireContext())

            AlertDialog.Builder(requireContext())
                .setTitle("New name")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->

                    val name = input.text.toString()

                    val updates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(updates)
                        ?.addOnCompleteListener {
                            txtUser.text = name
                        }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // ✅ ACHIEVEMENTS SAFE
        container.removeAllViews()

        val achievements = AchievementManager.getAllAchievements()

        for (a in achievements) {

            val tv = TextView(requireContext())
            tv.text = a.title
            tv.textSize = 16f
            tv.setPadding(20, 20, 20, 20)

            if (a.isUnlocked) {
                tv.setBackgroundColor(Color.parseColor("#4CAF50"))
                tv.setTextColor(Color.WHITE)
            } else {
                tv.setBackgroundColor(Color.parseColor("#DDDDDD"))
                tv.setTextColor(Color.DKGRAY)
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 16

            container.addView(tv, params)
        }
    }
}