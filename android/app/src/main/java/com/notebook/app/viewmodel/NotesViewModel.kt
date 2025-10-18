package com.notebook.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notebook.app.data.model.Note
import com.notebook.app.data.repository.NotesRepository
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    private val notesRepository = NotesRepository()

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadNotes(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = notesRepository.getNotes(userId)
            result.fold(
                onSuccess = { notesList ->
                    _notes.value = notesList
                    _loading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to load notes"
                    _loading.value = false
                }
            )
        }
    }

    fun refreshNotes(userId: String) {
        loadNotes(userId)
    }
}


