package com.notebook.app.ui.note

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.notebook.app.R
import com.notebook.app.data.model.Note
import com.notebook.app.databinding.ActivityNoteDetailBinding
import com.notebook.app.viewmodel.AuthViewModel
import com.notebook.app.viewmodel.NoteDetailViewModel
import java.io.File
import java.io.FileOutputStream

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteDetailBinding
    private val viewModel: NoteDetailViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var attachmentsAdapter: AttachmentsAdapter
    
    private var noteId: String? = null
    private var isNewNote = true

    companion object {
        const val EXTRA_NOTE_ID = "note_id"
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleFileSelection(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId = intent.getStringExtra(EXTRA_NOTE_ID)
        isNewNote = noteId == null

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

        if (!isNewNote) {
            noteId?.let { viewModel.loadNote(it) }
            supportActionBar?.title = "Edit Note"
        } else {
            supportActionBar?.title = "New Note"
            binding.attachFileButton.isEnabled = false
            updateAttachmentsVisibility(emptyList())
        }
    }

    private fun setupRecyclerView() {
        attachmentsAdapter = AttachmentsAdapter(
            onDeleteClick = { attachment ->
                showDeleteAttachmentConfirmation(attachment)
            }
        )

        binding.attachmentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@NoteDetailActivity)
            adapter = attachmentsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabSave.setOnClickListener {
            saveNote()
        }

        binding.attachFileButton.setOnClickListener {
            openFilePicker()
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.noteWithAttachments.observe(this) { noteData ->
            if (!isNewNote) {
                binding.titleEditText.setText(noteData.note.title)
                binding.contentEditText.setText(noteData.note.content)
                binding.attachFileButton.isEnabled = true
            }
            
            updateAttachmentsVisibility(noteData.attachments)
            attachmentsAdapter.submitList(noteData.attachments)
        }

        viewModel.loading.observe(this) { isLoading ->
            // Could show a progress indicator
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.saveSuccess.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT).show()
                
                if (isNewNote) {
                    // Update noteId and state after first save
                    val savedNote = viewModel.noteWithAttachments.value?.note
                    if (savedNote?.id != null) {
                        noteId = savedNote.id
                        isNewNote = false
                        supportActionBar?.title = "Edit Note"
                        binding.attachFileButton.isEnabled = true
                    }
                }
            } else if (success == false) {
                // Error already shown via error LiveData
            }
        }

        viewModel.deleteSuccess.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun saveNote() {
        val title = binding.titleEditText.text?.toString()?.trim() ?: ""
        val content = binding.contentEditText.text?.toString()?.trim() ?: ""

        if (title.isEmpty() || content.isEmpty()) {
            Snackbar.make(binding.root, "Title and content are required", Snackbar.LENGTH_SHORT).show()
            return
        }

        val userId = authViewModel.getCurrentUserId()
        if (userId == null) {
            Snackbar.make(binding.root, "Not authenticated", Snackbar.LENGTH_SHORT).show()
            return
        }

        val note = Note(
            id = noteId,
            userId = userId,
            title = title,
            content = content
        )

        if (isNewNote) {
            viewModel.createNote(note)
        } else {
            viewModel.updateNote(note)
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        filePickerLauncher.launch(Intent.createChooser(intent, "Select File"))
    }

    private fun handleFileSelection(uri: Uri) {
        val currentNoteId = noteId
        if (currentNoteId == null) {
            Snackbar.make(binding.root, "Please save the note first", Snackbar.LENGTH_SHORT).show()
            return
        }

        try {
            val fileName = getFileName(uri) ?: "unnamed_file"
            val mimeType = contentResolver.getType(uri)
            
            // Copy file to cache
            val tempFile = File(cacheDir, fileName)
            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            viewModel.uploadAttachment(currentNoteId, tempFile, fileName, mimeType)
            Toast.makeText(this, "Uploading file...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Snackbar.make(binding.root, "Failed to read file: ${e.message}", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex >= 0) {
                        result = cursor.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path?.let { path ->
                val cut = path.lastIndexOf('/')
                if (cut != -1) path.substring(cut + 1) else path
            }
        }
        return result
    }

    private fun updateAttachmentsVisibility(attachments: List<com.notebook.app.data.model.Attachment>) {
        if (attachments.isEmpty()) {
            binding.noAttachmentsText.visibility = View.VISIBLE
            binding.attachmentsRecyclerView.visibility = View.GONE
        } else {
            binding.noAttachmentsText.visibility = View.GONE
            binding.attachmentsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showDeleteAttachmentConfirmation(attachment: com.notebook.app.data.model.Attachment) {
        AlertDialog.Builder(this)
            .setTitle("Delete Attachment")
            .setMessage(getString(R.string.confirm_delete_file))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteAttachment(attachment)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!isNewNote) {
            menuInflater.inflate(R.menu.menu_note_detail, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteNoteConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteNoteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage(getString(R.string.confirm_delete_note))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                noteId?.let { viewModel.deleteNote(it) }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}


