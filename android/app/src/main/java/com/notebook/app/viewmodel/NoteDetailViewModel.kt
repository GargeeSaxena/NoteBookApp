package com.notebook.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notebook.app.data.model.Attachment
import com.notebook.app.data.model.Note
import com.notebook.app.data.model.NoteWithAttachments
import com.notebook.app.data.repository.NotesRepository
import kotlinx.coroutines.launch
import java.io.File

class NoteDetailViewModel : ViewModel() {
    private val notesRepository = NotesRepository()

    private val _noteWithAttachments = MutableLiveData<NoteWithAttachments>()
    val noteWithAttachments: LiveData<NoteWithAttachments> = _noteWithAttachments

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess

    fun loadNote(noteId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = notesRepository.getNoteWithAttachments(noteId)
            result.fold(
                onSuccess = { noteData ->
                    _noteWithAttachments.value = noteData
                    _loading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to load note"
                    _loading.value = false
                }
            )
        }
    }

    fun createNote(note: Note) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = notesRepository.createNote(note)
            result.fold(
                onSuccess = { createdNote ->
                    _noteWithAttachments.value = NoteWithAttachments(createdNote, emptyList())
                    _loading.value = false
                    _saveSuccess.value = true
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to create note"
                    _loading.value = false
                    _saveSuccess.value = false
                }
            )
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = notesRepository.updateNote(note)
            result.fold(
                onSuccess = { updatedNote ->
                    val current = _noteWithAttachments.value
                    _noteWithAttachments.value = NoteWithAttachments(
                        updatedNote,
                        current?.attachments ?: emptyList()
                    )
                    _loading.value = false
                    _saveSuccess.value = true
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to update note"
                    _loading.value = false
                    _saveSuccess.value = false
                }
            )
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = notesRepository.deleteNote(noteId)
            result.fold(
                onSuccess = {
                    _loading.value = false
                    _deleteSuccess.value = true
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to delete note"
                    _loading.value = false
                    _deleteSuccess.value = false
                }
            )
        }
    }

    fun uploadAttachment(noteId: String, file: File, fileName: String, mimeType: String?) {
        viewModelScope.launch {
            _error.value = null
            val result = notesRepository.uploadAttachment(noteId, file, fileName, mimeType)
            result.fold(
                onSuccess = { attachment ->
                    val current = _noteWithAttachments.value
                    if (current != null) {
                        val updatedAttachments = current.attachments + attachment
                        _noteWithAttachments.value = NoteWithAttachments(
                            current.note,
                            updatedAttachments
                        )
                    }
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to upload file"
                }
            )
        }
    }

    fun deleteAttachment(attachment: Attachment) {
        viewModelScope.launch {
            _error.value = null
            val result = notesRepository.deleteAttachment(attachment)
            result.fold(
                onSuccess = {
                    val current = _noteWithAttachments.value
                    if (current != null) {
                        val updatedAttachments = current.attachments.filter { it.id != attachment.id }
                        _noteWithAttachments.value = NoteWithAttachments(
                            current.note,
                            updatedAttachments
                        )
                    }
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to delete file"
                }
            )
        }
    }
}


