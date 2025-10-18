# Android App Features Documentation

This document details all implemented features and their usage.

## 🔐 Authentication

### Email/Password Authentication
- **Sign Up**: Create new account with email and password
- **Sign In**: Login with existing credentials
- **Session Management**: Automatic session persistence
- **Sign Out**: Secure logout with data cleanup

**Implementation Details:**
- Uses Supabase Auth SDK
- Passwords must be at least 6 characters
- Email validation included
- Session stored in SharedPreferences
- Automatic token refresh

**User Flow:**
1. App launches → Check if user is signed in
2. If not signed in → Show AuthActivity
3. User signs up/in → Navigate to MainActivity
4. Session persists until sign out

### Google OAuth (Partial)
- Opens Google OAuth URL in browser
- Requires additional setup (see README)
- Email/password is recommended for simpler setup

## 📝 Notes CRUD Operations

### Create Note
- Tap floating action button (+) on main screen
- Enter title and content
- Tap save button
- Note appears in list immediately

**Validation:**
- Title and content are required
- Maximum title length: 200 characters (database constraint)
- Content is unlimited

### Read Notes
- All user's notes displayed on main screen
- Sorted by creation date (newest first)
- Pull down to refresh
- Empty state shown when no notes exist

**Display:**
- Title (max 2 lines, ellipsized)
- Content preview (max 3 lines, ellipsized)
- Creation date (formatted)

### Update Note
- Tap any note to open detail view
- Edit title and/or content
- Tap save button
- Changes reflected immediately

### Delete Note
- Open note detail view
- Tap trash icon in toolbar
- Confirm deletion
- Note and all attachments deleted

**Cascade Deletion:**
- Deleting note removes all attachments
- Files deleted from Supabase Storage
- Database records removed

## 📎 File Attachments

### Upload File
1. Open note (must be saved first)
2. Tap "Attach File" button
3. Select file from device
4. File uploads automatically
5. Appears in attachments list

**Supported:**
- All file types
- Any file size (within Supabase limits)
- Multiple attachments per note

**Storage:**
- Files stored in Supabase Storage
- Bucket: `note-attachments`
- Path: `{noteId}/{timestamp}_{filename}`

### View Attachments
- Listed in note detail view
- Shows file name
- Icon indicates file type

### Delete Attachment
- Tap delete button on attachment
- Confirm deletion
- File removed from storage
- Database record deleted

**Note:** Cannot attach files to unsaved notes. Save the note first, then add attachments.

## 👤 User Profile & Premium Status

### Premium Status Display
- Shown on main screen in card at top
- Displays "Premium User" or "Free User"
- Retrieved from database on app launch
- Updated on pull-to-refresh

**Database Field:**
- Table: `users`
- Field: `is_premium` (boolean)
- Default: `false`

**Setting Premium Status:**
Since payments are not implemented, set manually:
```sql
UPDATE users 
SET is_premium = true 
WHERE id = 'user-id-here';
```

### User Profile
- Automatically created on sign up
- Stores: ID, email, display name, photo URL, premium status
- Synced with Supabase Auth

## 🎨 User Interface

### Main Screen (Notes List)
- **Toolbar**: App title, menu button
- **Premium Status Card**: Shows user tier
- **Notes List**: Scrollable list of all notes
- **Empty State**: Shown when no notes
- **FAB**: Floating action button to add note
- **Pull to Refresh**: Swipe down to refresh

### Note Detail Screen
- **Title Field**: Text input for note title
- **Content Field**: Multi-line text input
- **Attachments Section**: List of attached files
- **Attach File Button**: Opens file picker
- **Save FAB**: Floating action button to save
- **Delete Menu**: Trash icon in toolbar

### Auth Screen
- **Email Field**: Email input with validation
- **Password Field**: Password input with toggle
- **Sign In Button**: Primary action
- **Sign Up Button**: Secondary action
- **Google Sign In**: Alternative method
- **Loading State**: Progress indicator during auth

## 🔄 Data Synchronization

### Real-time Updates
- Not implemented (would require Supabase Realtime)
- Current: Manual refresh via pull-to-refresh
- Notes loaded on app launch
- Refreshed when returning to main screen

### Offline Support
- Not implemented (requires local caching)
- Current: Requires internet connection
- Graceful error handling for network issues

## 🎯 Architecture

### MVVM Pattern
- **Model**: Data classes (User, Note, Attachment)
- **View**: Activities and XML layouts
- **ViewModel**: Business logic and state management

### Repository Pattern
- `AuthRepository`: Authentication operations
- `NotesRepository`: Notes and attachments CRUD

### Dependency Flow
```
Activity → ViewModel → Repository → Supabase SDK
```

## 🔒 Security

### Authentication
- JWT tokens managed by Supabase SDK
- Automatic token refresh
- Secure session storage

### Data Access
- Row Level Security (RLS) enabled on database
- Users can only access their own notes
- File storage respects database relationships

### API Keys
- Supabase anon key is safe to embed
- RLS policies protect data
- No service key in client

## 📱 Compatibility

### Android Versions
- **Minimum**: Android 7.0 (API 24)
- **Target**: Android 15 (API 35)
- **Tested**: Android 8.0+ recommended

### Screen Sizes
- Phone: Full support
- Tablet: Supported (phone layout)
- Landscape: Supported

### Features Required
- Internet connection (required)
- Storage permission (for file uploads on Android <13)
- Camera permission (not required, but useful for photos)

## 🚀 Performance

### Optimization
- RecyclerView with DiffUtil for efficient list updates
- Coroutines for async operations
- View binding for efficient view access
- Minimal memory footprint

### Loading States
- Progress indicators during operations
- Pull-to-refresh for manual updates
- Empty states for better UX

## 📊 Limitations

As per requirements:

1. **No Payments**: DodoPayments not implemented
   - Premium status is read-only from database
   - Must be set manually via SQL

2. **No Real-time Sync**: 
   - Manual refresh required
   - Could be added with Supabase Realtime

3. **No Offline Mode**:
   - Requires internet connection
   - Could be added with local database (Room)

4. **Google OAuth Incomplete**:
   - Opens browser but doesn't handle callback
   - Requires deep link setup for full functionality
   - Email/password works perfectly

## 🔮 Future Enhancements (Not Implemented)

Potential features for future development:
- Rich text editing (bold, italic, lists)
- Image preview for image attachments
- File download functionality
- Share notes feature
- Search and filter notes
- Tags/categories for notes
- Dark mode (theme is ready, needs toggle)
- Biometric authentication
- Note templates
- Export notes as PDF
- Collaborative editing
- Voice notes
- Reminders/notifications

## 📚 Code Examples

### Creating a Note
```kotlin
val note = Note(
    userId = userId,
    title = "My Note",
    content = "Note content here"
)
viewModel.createNote(note)
```

### Uploading Attachment
```kotlin
val file = File(path)
viewModel.uploadAttachment(
    noteId = noteId,
    file = file,
    fileName = "document.pdf",
    mimeType = "application/pdf"
)
```

### Observing Data
```kotlin
viewModel.notes.observe(this) { notes ->
    adapter.submitList(notes)
}
```

---

For setup instructions, see [README.md](./README.md)

For quick start guide, see [SETUP_GUIDE.md](./SETUP_GUIDE.md)


