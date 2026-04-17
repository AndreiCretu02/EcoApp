package com.example.ecoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // 🔥 IMPORTANT: Replace with your Firebase Web Client ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID_HERE")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnGoogle = findViewById<Button>(R.id.btnGoogle)

        // 🔹 LOGIN
        btnLogin.setOnClickListener {
            val e = email.text.toString()
            val p = password.text.toString()

            if (e.isEmpty() || p.isEmpty()) {
                toast("Fill all fields")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToMain()
                    } else {
                        toast("Login failed")
                    }
                }
        }

        // 🔹 REGISTER
        btnRegister.setOnClickListener {
            val e = email.text.toString()
            val p = password.text.toString()

            if (e.isEmpty() || p.length < 6) {
                toast("Password must be at least 6 chars")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToMain()
                    } else {
                        toast("Register failed")
                    }
                }
        }

        // 🔹 GOOGLE LOGIN
        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(Exception::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            goToMain()
                        } else {
                            toast("Google login failed")
                        }
                    }
            } catch (e: Exception) {
                toast("Google sign-in failed: ${e.message}")
            }
        }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        // 🔥 AUTO LOGIN
        if (auth.currentUser != null) {
            goToMain()
        }
    }
}