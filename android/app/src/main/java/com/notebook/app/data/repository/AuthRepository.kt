package com.notebook.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.notebook.app.NotebookApplication
import com.notebook.app.data.SupabaseClientInstance
import com.notebook.app.data.model.User
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val client = SupabaseClientInstance.client
    private val auth = SupabaseClientInstance.auth
    
    private val prefs: SharedPreferences = 
        NotebookApplication.instance.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    suspend fun signInWithEmail(email: String, password: String): Result<UserInfo> = withContext(Dispatchers.IO) {
        try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val user = auth.currentUserOrNull()
            if (user != null) {
                saveUserId(user.id)
                upsertUserProfile(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Sign in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmail(email: String, password: String): Result<UserInfo> = withContext(Dispatchers.IO) {
        try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val user = auth.currentUserOrNull()
            if (user != null) {
                saveUserId(user.id)
                upsertUserProfile(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Sign up failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Note: Google OAuth in Android requires additional setup
            // This returns the OAuth URL that should be opened in a browser
            // For a complete implementation, you'd need to handle the OAuth flow
            // using a custom tab or WebView
            val url = auth.signInWith(Google).toString()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signOut()
            clearUserId()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? {
        // Check Supabase session first (source of truth)
        val supabaseUserId = auth.currentUserOrNull()?.id
        
        if (supabaseUserId != null) {
            // Valid session exists, save/update it
            saveUserId(supabaseUserId)
            return supabaseUserId
        }
        
        // No valid Supabase session, clear stored ID
        clearUserId()
        return null
    }

    fun isSignedIn(): Boolean {
        return auth.currentUserOrNull() != null
    }

    private fun saveUserId(userId: String) {
        prefs.edit().putString("user_id", userId).apply()
    }

    private fun clearUserId() {
        prefs.edit().remove("user_id").apply()
    }

    private suspend fun upsertUserProfile(userInfo: UserInfo) {
        try {
            client.from("users").upsert(
                User(
                    id = userInfo.id,
                    email = userInfo.email,
                    displayName = userInfo.userMetadata?.get("full_name")?.toString(),
                    photoUrl = userInfo.userMetadata?.get("avatar_url")?.toString(),
                    isPremium = false,
                    createdAt = null,
                    updatedAt = null
                )
            ) {
                select()
            }
        } catch (e: Exception) {
            // Log but don't fail on profile upsert error
            e.printStackTrace()
        }
    }

    suspend fun getUserProfile(userId: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val result = client.from("users")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<User>()
            Result.success(result)
        } catch (e: RestException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


