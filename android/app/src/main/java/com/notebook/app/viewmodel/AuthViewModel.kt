package com.notebook.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notebook.app.data.model.User
import com.notebook.app.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> = _userProfile

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (authRepository.isSignedIn()) {
            val userId = authRepository.getCurrentUserId()
            if (userId != null) {
                loadUserProfile(userId)
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithEmail(email, password)
            result.fold(
                onSuccess = { user ->
                    loadUserProfile(user.id)
                    _authState.value = AuthState.Success
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Sign in failed")
                }
            )
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signUpWithEmail(email, password)
            result.fold(
                onSuccess = { user ->
                    loadUserProfile(user.id)
                    _authState.value = AuthState.Success
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Sign up failed")
                }
            )
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithGoogle()
            result.fold(
                onSuccess = { url ->
                    _authState.value = AuthState.GoogleOAuthUrl(url)
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Google sign in failed")
                }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _userProfile.value = null
            _authState.value = AuthState.SignedOut
        }
    }

    private fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            val result = authRepository.getUserProfile(userId)
            result.fold(
                onSuccess = { user ->
                    _userProfile.value = user
                },
                onFailure = {
                    // Failed to load profile, but auth was successful
                    _userProfile.value = null
                }
            )
        }
    }

    fun getCurrentUserId(): String? {
        return authRepository.getCurrentUserId()
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object SignedOut : AuthState()
    data class Error(val message: String) : AuthState()
    data class GoogleOAuthUrl(val url: String) : AuthState()
}


