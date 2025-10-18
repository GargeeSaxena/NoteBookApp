# Adding File Attachments to Web App (Optional)

The Android app now supports file attachments. If you want to add this feature to your web app, here's how:

## 📋 What's Already Done

✅ Database table (`attachments`) created
✅ Supabase Storage bucket (`note-attachments`) set up
✅ Android app uploading and displaying files
✅ Files stored with organized path structure

## 🔧 What You Need to Add to Web App

### 1. Update HTML (index.html)

Add to your note detail/edit view:

```html
<!-- Add this to your note form -->
<div class="attachments-section">
  <h3>Attachments</h3>
  <div id="attachmentsList"></div>
  <input type="file" id="fileInput" style="display: none;" />
  <button id="attachFileBtn" class="btn-secondary">
    📎 Attach File
  </button>
</div>
```

### 2. Update JavaScript (script.js)

Add these functions:

```javascript
// Fetch attachments for a note
async function fetchAttachments(noteId) {
  try {
    const { data, error } = await supabase
      .from('attachments')
      .select('*')
      .eq('note_id', noteId)
      .order('created_at', { ascending: false });
    
    if (error) throw error;
    return data || [];
  } catch (err) {
    console.error('Error fetching attachments:', err);
    return [];
  }
}

// Upload a file
async function uploadFile(noteId, file) {
  try {
    // Generate unique file path
    const filePath = `${noteId}/${Date.now()}_${file.name}`;
    
    // Upload to storage
    const { error: uploadError } = await supabase.storage
      .from('note-attachments')
      .upload(filePath, file);
    
    if (uploadError) throw uploadError;
    
    // Create attachment record
    const { data, error: dbError } = await supabase
      .from('attachments')
      .insert({
        note_id: noteId,
        file_name: file.name,
        file_path: filePath,
        file_size: file.size,
        mime_type: file.type
      })
      .select()
      .single();
    
    if (dbError) throw dbError;
    return data;
  } catch (err) {
    console.error('Error uploading file:', err);
    throw err;
  }
}

// Delete an attachment
async function deleteAttachment(attachment) {
  try {
    // Delete from storage
    const { error: storageError } = await supabase.storage
      .from('note-attachments')
      .remove([attachment.file_path]);
    
    if (storageError) console.error('Storage delete error:', storageError);
    
    // Delete record
    const { error: dbError } = await supabase
      .from('attachments')
      .delete()
      .eq('id', attachment.id);
    
    if (dbError) throw dbError;
  } catch (err) {
    console.error('Error deleting attachment:', err);
    throw err;
  }
}

// Get public URL for file
function getFileUrl(filePath) {
  const { data } = supabase.storage
    .from('note-attachments')
    .getPublicUrl(filePath);
  
  return data.publicUrl;
}

// Render attachments list
function renderAttachments(attachments) {
  const container = document.getElementById('attachmentsList');
  
  if (!attachments || attachments.length === 0) {
    container.innerHTML = '<p class="text-muted">No attachments</p>';
    return;
  }
  
  container.innerHTML = attachments.map(att => `
    <div class="attachment-item" data-id="${att.id}">
      <span class="attachment-name">
        📎 ${escapeHtml(att.file_name)}
      </span>
      <span class="attachment-size">
        ${formatFileSize(att.file_size)}
      </span>
      <div class="attachment-actions">
        <button onclick="downloadAttachment('${att.file_path}')" 
                class="btn-icon">
          ⬇️ Download
        </button>
        <button onclick="confirmDeleteAttachment('${att.id}')" 
                class="btn-icon btn-danger">
          🗑️ Delete
        </button>
      </div>
    </div>
  `).join('');
}

// Format file size
function formatFileSize(bytes) {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
}

// Download attachment
async function downloadAttachment(filePath) {
  const url = getFileUrl(filePath);
  window.open(url, '_blank');
}

// Confirm and delete attachment
async function confirmDeleteAttachment(attachmentId) {
  if (!confirm('Delete this attachment?')) return;
  
  const attachment = currentAttachments.find(a => a.id === attachmentId);
  if (!attachment) return;
  
  try {
    await deleteAttachment(attachment);
    await loadNoteAttachments(currentNoteId); // Refresh list
    alert('Attachment deleted');
  } catch (err) {
    alert('Failed to delete attachment: ' + err.message);
  }
}

// Event listener for file input
document.addEventListener('DOMContentLoaded', () => {
  const fileInput = document.getElementById('fileInput');
  const attachBtn = document.getElementById('attachFileBtn');
  
  attachBtn.addEventListener('click', () => {
    fileInput.click();
  });
  
  fileInput.addEventListener('change', async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    
    if (!currentNoteId) {
      alert('Please save the note first before attaching files.');
      return;
    }
    
    try {
      attachBtn.disabled = true;
      attachBtn.textContent = '⏳ Uploading...';
      
      await uploadFile(currentNoteId, file);
      await loadNoteAttachments(currentNoteId);
      
      alert('File uploaded successfully!');
      fileInput.value = ''; // Reset input
    } catch (err) {
      alert('Failed to upload file: ' + err.message);
    } finally {
      attachBtn.disabled = false;
      attachBtn.textContent = '📎 Attach File';
    }
  });
});

// Load attachments when loading a note
async function loadNoteAttachments(noteId) {
  currentNoteId = noteId;
  const attachments = await fetchAttachments(noteId);
  currentAttachments = attachments;
  renderAttachments(attachments);
}
```

### 3. Update CSS (styles.css)

Add styles for attachments:

```css
/* Attachments Section */
.attachments-section {
  margin-top: 24px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 8px;
}

.attachment-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: white;
  border-radius: 4px;
  margin-bottom: 8px;
}

.attachment-name {
  flex: 1;
  font-weight: 500;
}

.attachment-size {
  color: #666;
  font-size: 0.9em;
}

.attachment-actions {
  display: flex;
  gap: 8px;
}

.btn-icon {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  background: #e0e0e0;
  cursor: pointer;
  font-size: 0.9em;
}

.btn-icon:hover {
  background: #d0d0d0;
}

.btn-danger {
  background: #ffebee;
  color: #c62828;
}

.btn-danger:hover {
  background: #ffcdd2;
}
```

### 4. Integration Points

Update your existing note loading/editing functions:

```javascript
// When loading a note
async function loadNote(noteId) {
  // ... existing code to load note data ...
  
  // Add this:
  await loadNoteAttachments(noteId);
}

// When creating a new note
async function createNote(title, content) {
  // ... existing code to create note ...
  
  const newNote = // ... created note with ID
  
  // Now enable file attachments
  currentNoteId = newNote.id;
  document.getElementById('attachFileBtn').disabled = false;
}

// When deleting a note
async function deleteNote(noteId) {
  // Get attachments first
  const attachments = await fetchAttachments(noteId);
  
  // Delete all attachments
  for (const attachment of attachments) {
    await deleteAttachment(attachment);
  }
  
  // ... existing code to delete note ...
}
```

## 🎯 Features You'll Get

After implementing this:
- ✅ View files attached by Android app
- ✅ Upload files from web app
- ✅ Download files
- ✅ Delete files
- ✅ See file names and sizes
- ✅ Full compatibility with Android app

## 🔒 Security Considerations

Make sure your storage bucket has proper policies:

```sql
-- In Supabase Storage policies
-- Allow authenticated users to upload
create policy "Users can upload files"
on storage.objects for insert
to authenticated
with check (bucket_id = 'note-attachments');

-- Allow public read (or restrict to authenticated)
create policy "Public can read files"
on storage.objects for select
to public
using (bucket_id = 'note-attachments');

-- Allow users to delete their own files
create policy "Users can delete files"
on storage.objects for delete
to authenticated
using (bucket_id = 'note-attachments');
```

## 📝 Testing

1. Create a note on web app
2. Upload a file
3. Refresh page - file should still be there
4. Open Android app, view same note - file should be visible
5. Delete file on web - should be removed everywhere

## 🚀 Optional Enhancements

### File Preview
For images:
```javascript
function renderImagePreview(attachment) {
  if (attachment.mime_type?.startsWith('image/')) {
    const url = getFileUrl(attachment.file_path);
    return `<img src="${url}" class="attachment-preview" />`;
  }
  return '';
}
```

### Drag & Drop
```javascript
const dropZone = document.getElementById('noteContent');

dropZone.addEventListener('dragover', (e) => {
  e.preventDefault();
  dropZone.classList.add('drag-over');
});

dropZone.addEventListener('drop', async (e) => {
  e.preventDefault();
  dropZone.classList.remove('drag-over');
  
  const file = e.dataTransfer.files[0];
  if (file && currentNoteId) {
    await uploadFile(currentNoteId, file);
    await loadNoteAttachments(currentNoteId);
  }
});
```

### File Type Icons
```javascript
function getFileIcon(mimeType) {
  if (mimeType.startsWith('image/')) return '🖼️';
  if (mimeType.startsWith('video/')) return '🎥';
  if (mimeType.startsWith('audio/')) return '🎵';
  if (mimeType.includes('pdf')) return '📄';
  if (mimeType.includes('zip') || mimeType.includes('rar')) return '📦';
  return '📎';
}
```

## ✅ Checklist

- [ ] Add HTML elements for attachments
- [ ] Add JavaScript functions
- [ ] Add CSS styles
- [ ] Integrate with existing note functions
- [ ] Test upload functionality
- [ ] Test download functionality
- [ ] Test delete functionality
- [ ] Test cross-platform (Android ↔ Web)
- [ ] Update storage bucket policies

## 📚 References

- [Supabase Storage Docs](https://supabase.com/docs/guides/storage)
- [Supabase JavaScript SDK](https://supabase.com/docs/reference/javascript/storage-from-upload)

---

This is optional but recommended for feature parity with the Android app!


