package com.notebook.app.data.repository

import com.notebook.app.data.SupabaseClientInstance
import com.notebook.app.data.model.Attachment
import com.notebook.app.data.model.Note
import com.notebook.app.data.model.NoteWithAttachments
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class NotesRepository {
    private val client = SupabaseClientInstance.client
    private val storage = SupabaseClientInstance.storage

    companion object {
        private const val STORAGE_BUCKET = "note-attachments"
    }

    suspend fun getNotes(userId: String): Result<List<Note>> = withContext(Dispatchers.IO) {
        try {
            val notes = client.from("notes")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                    order(column = "created_at", order = Order.DESCENDING)
                }
                .decodeList<Note>()
            Result.success(notes)
        } catch (e: RestException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNoteWithAttachments(noteId: String): Result<NoteWithAttachments> = withContext(Dispatchers.IO) {
        try {
            // Get note
            val note = client.from("notes")
                .select {
                    filter {
                        eq("id", noteId)
                    }
                }
                .decodeSingle<Note>()

            // Get attachments
            val attachments = try {
                client.from("attachments")
                    .select {
                        filter {
                            eq("note_id", noteId)
                        }
                    }
                    .decodeList<Attachment>()
            } catch (e: Exception) {
                emptyList()
            }

            Result.success(NoteWithAttachments(note, attachments))
        } catch (e: RestException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createNote(note: Note): Result<Note> = withContext(Dispatchers.IO) {
        try {
            val result = client.from("notes")
                .insert(note) {
                    select()
                }
                .decodeSingle<Note>()
            Result.success(result)
        } catch (e: RestException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNote(note: Note): Result<Note> = withContext(Dispatchers.IO) {
        try {
            val result = client.from("notes")
                .update(
                    {
                        set("title", note.title)
                        set("content", note.content)
                    }
                ) {
                    filter {
                        eq("id", note.id!!)
                    }
                    select()
                }
                .decodeSingle<Note>()
            Result.success(result)
        } catch (e: RestException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNote(noteId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Delete attachments from storage first
            val attachments = client.from("attachments")
                .select {
                    filter {
                        eq("note_id", noteId)
                    }
                }
                .decodeList<Attachment>()

            attachments.forEach { attachment ->
                try {
                    storage.from(STORAGE_BUCKET).delete(attachment.filePath)
                } catch (e: Exception) {
                    // Continue even if deletion fails
                    e.printStackTrace()
                }
            }

            // Delete note (attachments will be deleted by cascade)
            client.from("notes")
                .delete {
                    filter {
                        eq("id", noteId)
                    }
                }
            Result.success(Unit)
        } catch (e: RestException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadAttachment(
        noteId: String,
        file: File,
        fileName: String,
        mimeType: String?
    ): Result<Attachment> = withContext(Dispatchers.IO) {
        try {
            // Generate unique file path
            val filePath = "$noteId/${System.currentTimeMillis()}_$fileName"

            // Upload to storage
            storage.from(STORAGE_BUCKET).upload(filePath, file.readBytes())

            // Create attachment record
            val attachment = Attachment(
                noteId = noteId,
                fileName = fileName,
                filePath = filePath,
                fileSize = file.length(),
                mimeType = mimeType
            )

            val result = client.from("attachments")
                .insert(attachment)
                .decodeSingle<Attachment>()

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAttachment(attachment: Attachment): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Delete from storage
            storage.from(STORAGE_BUCKET).delete(attachment.filePath)

            // Delete record
            client.from("attachments")
                .delete {
                    filter {
                        eq("id", attachment.id!!)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAttachmentUrl(filePath: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = storage.from(STORAGE_BUCKET).publicUrl(filePath)
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


