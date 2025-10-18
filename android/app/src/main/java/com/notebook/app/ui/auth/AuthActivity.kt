package com.notebook.app.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.google.android.material.snackbar.Snackbar
import com.notebook.app.databinding.ActivityAuthBinding
import com.notebook.app.ui.MainActivity
import com.notebook.app.viewmodel.AuthState
import com.notebook.app.viewmodel.AuthViewModel

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if already signed in
        if (viewModel.getCurrentUserId() != null) {
            navigateToMain()
            return
        }

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text?.toString()?.trim() ?: ""
            val password = binding.passwordEditText.text?.toString() ?: ""

            if (validateInput(email, password)) {
                viewModel.signInWithEmail(email, password)
            }
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text?.toString()?.trim() ?: ""
            val password = binding.passwordEditText.text?.toString() ?: ""

            if (validateInput(email, password)) {
                viewModel.signUpWithEmail(email, password)
            }
        }

        binding.googleSignInButton.setOnClickListener {
            viewModel.signInWithGoogle()
        }
    }

    private fun observeViewModel() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    showLoading(true)
                }
                is AuthState.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
                is AuthState.Error -> {
                    showLoading(false)
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                }
                is AuthState.GoogleOAuthUrl -> {
                    showLoading(false)
                    openGoogleOAuth(state.url)
                }
                is AuthState.SignedOut -> {
                    // Do nothing, already on auth screen
                }
                else -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(binding.root, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(binding.root, "Invalid email address", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Snackbar.make(binding.root, "Password must be at least 6 characters", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.signInButton.isEnabled = !isLoading
        binding.signUpButton.isEnabled = !isLoading
        binding.googleSignInButton.isEnabled = !isLoading
    }

    private fun openGoogleOAuth(url: String) {
        try {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(this, Uri.parse(url))
            
            Toast.makeText(
                this,
                "Note: Google OAuth requires additional setup. Please see documentation.",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Snackbar.make(
                binding.root,
                "Failed to open browser: ${e.message}",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}


