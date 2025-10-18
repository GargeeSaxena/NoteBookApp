package com.notebook.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.notebook.app.R
import com.notebook.app.databinding.ActivityMainBinding
import com.notebook.app.ui.auth.AuthActivity
import com.notebook.app.ui.note.NoteDetailActivity
import com.notebook.app.viewmodel.AuthViewModel
import com.notebook.app.viewmodel.NotesViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check authentication
        val userId = authViewModel.getCurrentUserId()
        if (userId == null || userId.isEmpty() || userId == "null") {
            navigateToAuth()
            return
        }

        setupRecyclerView()
        setupClickListeners()
        observeViewModels()

        // Load notes
        notesViewModel.loadNotes(userId)
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter { note ->
            val intent = Intent(this, NoteDetailActivity::class.java)
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)
            startActivity(intent)
        }

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java)
            startActivity(intent)
        }

        binding.swipeRefresh.setOnRefreshListener {
            val userId = authViewModel.getCurrentUserId()
            if (userId != null) {
                notesViewModel.refreshNotes(userId)
            }
        }

        binding.btnSignOut.setOnClickListener {
            showSignOutConfirmation()
        }
    }

    private fun observeViewModels() {
        // Observe user profile for premium status
        authViewModel.userProfile.observe(this) { user ->
            if (user != null) {
                binding.premiumStatusText.text = if (user.isPremium) {
                    getString(R.string.premium_user)
                } else {
                    getString(R.string.free_user)
                }
                binding.premiumStatusCard.visibility = View.VISIBLE
            } else {
                binding.premiumStatusCard.visibility = View.GONE
            }
        }

        // Observe notes
        notesViewModel.notes.observe(this) { notes ->
            notesAdapter.submitList(notes)
            binding.emptyState.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }

        notesViewModel.loading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        notesViewModel.error.observe(this) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh notes when returning to this activity
        val userId = authViewModel.getCurrentUserId()
        if (userId != null) {
            notesViewModel.refreshNotes(userId)
            authViewModel.checkAuthStatus()
        }
    }

    private fun showSignOutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Sign Out") { _, _ ->
                authViewModel.signOut()
                navigateToAuth()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}


