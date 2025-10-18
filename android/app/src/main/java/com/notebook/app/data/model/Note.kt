package com.notebook.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    val title: String,
    val content: String,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

data class NoteWithAttachments(
    val note: Note,
    val attachments: List<Attachment> = emptyList()
)


