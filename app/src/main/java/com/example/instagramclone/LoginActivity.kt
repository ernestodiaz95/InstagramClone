package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.databinding.ActivityLoginBinding
import com.parse.ParseUser

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        if (ParseUser.getCurrentUser() != null) {
            launchMainActivity()
        }

        val etUsername = loginBinding.etUsername
        val etPassword = loginBinding.etPassword
        val btnLogin = loginBinding.btnLogin
        val btnSignUp = loginBinding.btnSignUp

        btnLogin.setOnClickListener() {
            Log.i(TAG, "onClick login button")
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            loginUser(username, password)
        }

        btnSignUp.setOnClickListener {
            Log.i(TAG, "onClick sign up button")
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            signUpUser(username, password)
        }
    }

    private fun signUpUser(username: String, password: String) {
        Log.i(TAG, "Attempting to sign up user $username")
        val user = ParseUser()
        // Set the user's username and password
        user.username = username
        user.setPassword(password)
        user.signUpInBackground {
            if (it == null) {
                loginUser(username, password)
            } else {
                Log.e(TAG,"Issue with sign up: ${it.message}")
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        Log.i(TAG, "Attempting to login user $username")
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                // navigate to the main activity if the user has signed in properly
                launchMainActivity()
                Toast.makeText(this@LoginActivity, "You are now logged in", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG,"Issue with login", e)
            }
        }))
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}