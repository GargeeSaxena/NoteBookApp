package com.notebook.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String?,
    @SerialName("display_name")
    val displayName: String?,
    @SerialName("photo_url")
    val photoUrl: String?,
    @SerialName("is_premium")
    val isPremium: Boolean = false,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("updated_at")
    val updatedAt: String?
)


