package com.notebook.app.data

import com.notebook.app.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

object SupabaseClientInstance {
    // Supabase credentials are loaded from local.properties via BuildConfig
    // See android/local.properties.example for setup instructions
    private val SUPABASE_URL = BuildConfig.SUPABASE_URL
    private val SUPABASE_ANON_KEY = BuildConfig.SUPABASE_ANON_KEY

    val client: SupabaseClient by lazy {
        require(SUPABASE_URL.isNotEmpty()) { 
            "SUPABASE_URL is not configured. Please add it to android/local.properties" 
        }
        require(SUPABASE_ANON_KEY.isNotEmpty()) { 
            "SUPABASE_ANON_KEY is not configured. Please add it to android/local.properties" 
        }
        
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
        }
    }

    val auth: Auth
        get() = client.auth

    val postgrest: Postgrest
        get() = client.postgrest

    val storage: Storage
        get() = client.storage
}


