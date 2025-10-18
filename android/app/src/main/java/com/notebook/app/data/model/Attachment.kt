package com.notebook.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val id: String? = null,
    @SerialName("note_id")
    val noteId: String,
    @SerialName("file_name")
    val fileName: String,
    @SerialName("file_path")
    val filePath: String,
    @SerialName("file_size")
    val fileSize: Long,
    @SerialName("mime_type")
    val mimeType: String?,
    @SerialName("created_at")
    val createdAt: String? = null
)


