# Integration Guide - Android App with Existing Backend

This guide explains how the Android app integrates with your existing NoteBook app backend.

## 🔗 Architecture Overview

```
┌─────────────────┐
│  Android App    │
│   (Kotlin)      │
└────────┬────────┘
         │
         │ Direct Connection
         │
         ▼
┌─────────────────┐      ┌──────────────┐
│    Supabase     │◄─────┤   Web App    │
│   (Backend)     │      │ (JavaScript) │
└─────────────────┘      └──────────────┘
         │
         ▼
┌─────────────────┐
│   PostgreSQL    │
│   (Database)    │
└─────────────────┘
```

## 📊 Database Schema Compatibility

The Android app uses the **same database schema** as the web app with two additions:

### Existing Tables (Used by Android)
```sql
-- notes table (existing)
-- Android reads and writes to this table
-- Fully compatible with web app

-- users table (existing) 
-- Android reads and writes to this table
-- Enhanced with is_premium field
```

### New Tables (Android-specific)
```sql
-- attachments table (new)
-- Stores file attachment metadata
-- Does not affect web app functionality

-- Added column to users table:
ALTER TABLE users ADD COLUMN is_premium BOOLEAN DEFAULT false;
```

## 🔄 Data Flow

### Authentication
1. **Android**: User signs in via Supabase Auth
2. **Supabase**: Returns JWT token + user ID
3. **Android**: Stores user ID locally
4. **Database**: Creates/updates user record in `users` table
5. **Web App**: Can see the same user if they sign in

### Notes Creation
1. **Android**: User creates note
2. **Android**: Sends to Supabase with user_id
3. **Supabase**: Inserts into `notes` table
4. **Web App**: Can immediately see this note (after refresh)

### File Attachments
1. **Android**: User uploads file
2. **Android**: Uploads to Supabase Storage (`note-attachments` bucket)
3. **Android**: Creates record in `attachments` table
4. **Web App**: Could display attachments if implemented

## 🔐 Authentication Compatibility

Both platforms use **Supabase Auth**:

| Feature | Android | Web App | Compatible? |
|---------|---------|---------|-------------|
| Email/Password | ✅ | ✅ | ✅ Yes |
| Google OAuth | ⚠️ Partial | ✅ | ✅ Yes* |
| Session Token | ✅ | ✅ | ✅ Yes |
| User Profile | ✅ | ✅ | ✅ Yes |

*OAuth works but requires platform-specific setup

### Shared User Data
- Same user can sign in on both platforms
- User ID is consistent across platforms
- Email and profile data synchronized

## 📝 Notes Data Structure

Both platforms use identical note structure:

```json
{
  "id": "uuid",
  "user_id": "string",
  "title": "string",
  "content": "string",
  "created_at": "timestamp",
  "updated_at": "timestamp"
}
```

**100% Compatible** ✅

## 📎 File Attachments

### Android Implementation
```json
{
  "id": "uuid",
  "note_id": "uuid",
  "file_name": "string",
  "file_path": "string",
  "file_size": "bigint",
  "mime_type": "string",
  "created_at": "timestamp"
}
```

### Web App Compatibility
The web app **does not currently** implement file attachments, but:
- ✅ Database schema is ready
- ✅ Storage bucket exists
- ✅ Files stored by Android are accessible
- ⚠️ Web app would need to be updated to display them

### Adding Attachments to Web App (Optional)

To display Android-uploaded files in web app:

```javascript
// Fetch attachments for a note
async function getAttachments(noteId) {
  const { data, error } = await supabase
    .from('attachments')
    .select('*')
    .eq('note_id', noteId);
  
  return data;
}

// Get file URL
const { data } = supabase.storage
  .from('note-attachments')
  .getPublicUrl(attachment.file_path);
```

## 💎 Premium Status

### Current Implementation
- **Android**: Reads `is_premium` from database, displays status
- **Web App**: Could also read this field
- **Payments**: Not implemented (as requested)

### Manual Premium Assignment
```sql
-- Set user as premium
UPDATE users 
SET is_premium = true 
WHERE email = 'user@example.com';

-- Remove premium
UPDATE users 
SET is_premium = false 
WHERE email = 'user@example.com';
```

### Future Payment Integration (Not Implemented)
When implementing payments:
1. Payment processor (DodoPayments/Stripe) confirms payment
2. Backend webhook updates `is_premium` field
3. Android app reads updated status
4. Web app reads updated status

## 🔄 Synchronization

### Current Behavior
- ✅ Notes created on Android appear on web (after refresh)
- ✅ Notes created on web appear on Android (after refresh)
- ✅ Edits on either platform sync to database
- ✅ Deletes on either platform sync to database
- ❌ No real-time sync (would require Supabase Realtime)

### Enabling Real-time Sync (Optional)

To enable live updates between platforms:

1. **Enable Supabase Realtime** in your project
2. **Android**: Subscribe to notes changes
```kotlin
supabase.from("notes").on(SupabaseEventType.INSERT) { note ->
  // Update UI
}.subscribe()
```

3. **Web App**: Already has potential for realtime via Supabase JS

## 🗄️ Backend API Usage

The Android app **does not** use your Express.js server (`server.js`).

Instead:
- ✅ Android → Direct connection → Supabase
- ✅ Web App → Can use Express server OR direct Supabase
- ✅ Both platforms share the same database

### Why No Express Server?
- Simpler architecture for mobile
- Reduced latency (one less hop)
- Native Supabase SDK features
- Express server still valuable for web app deployment

### If You Need Express for Android
You could optionally route Android through Express:

```kotlin
// Instead of Supabase SDK
val response = httpClient.post("https://your-server.com/api/notes") {
  headers {
    append("X-User-Id", userId)
  }
  setBody(note)
}
```

But direct Supabase is recommended.

## 🔒 Security Considerations

### Row Level Security (RLS)
Both platforms rely on RLS policies:

```sql
-- Example: Users can only see their own notes
CREATE POLICY "Users see own notes" 
ON notes FOR SELECT 
USING (user_id = current_setting('request.jwt.claim.sub')::text);
```

**Important**: When using direct Supabase connection (Android), RLS policies MUST be properly configured.

### API Keys
- **Anon Key**: Safe to use in Android app (public)
- **Service Key**: NEVER embed in Android app
- RLS protects data even with anon key

### Authentication Tokens
- JWT tokens managed by Supabase SDK
- Automatically included in requests
- Validated by Supabase backend
- Expired tokens refreshed automatically

## 📱 Testing Cross-Platform

### Test Scenario 1: Create on Android, View on Web
1. Open Android app, sign in
2. Create a note with title "Test from Android"
3. Open web app in browser, sign in with same account
4. Refresh page
5. ✅ Note should appear

### Test Scenario 2: Create on Web, View on Android
1. Open web app, sign in
2. Create a note
3. Open Android app, sign in with same account
4. Pull to refresh
5. ✅ Note should appear

### Test Scenario 3: Edit on Web, View on Android
1. Edit a note on web app
2. Pull to refresh on Android
3. ✅ Changes should appear

### Test Scenario 4: Delete on Android, Verify on Web
1. Delete a note on Android
2. Refresh web app
3. ✅ Note should be gone

## 🐛 Common Integration Issues

### Issue: Notes not syncing
**Cause**: Different user IDs or not signed in
**Solution**: Ensure using same email/password on both platforms

### Issue: Android can't connect to Supabase
**Cause**: Wrong credentials in SupabaseClient.kt
**Solution**: Copy exact URL and anon key from Supabase dashboard

### Issue: Attachments not working
**Cause**: Storage bucket not created
**Solution**: Create `note-attachments` bucket in Supabase Storage

### Issue: Premium status not showing
**Cause**: Migration not run
**Solution**: Run `2025-10-18_add_premium_and_attachments.sql`

## 📊 Database Migration Path

If you need to update the database:

1. Create migration SQL file
2. Test on development database
3. Run on production database
4. Update both Android and Web apps if needed

Both platforms should remain compatible as long as:
- ✅ Existing columns not removed
- ✅ Existing tables not dropped
- ✅ New columns have defaults
- ✅ RLS policies maintained

## 🚀 Deployment Considerations

### Web App
- Deployed on Render (or other host)
- Serves static files
- Proxies to Supabase (optional)

### Android App
- Distributed via APK or Google Play Store
- Connects directly to Supabase
- No server deployment needed

### Database
- Hosted on Supabase
- Shared by both platforms
- Single source of truth

## 📚 Summary

| Aspect | Status | Notes |
|--------|--------|-------|
| Database Schema | ✅ Compatible | Minor additions only |
| Authentication | ✅ Shared | Same Supabase Auth |
| Notes CRUD | ✅ Fully Synced | Both read/write same table |
| File Attachments | ⚠️ Android Only | Web could be updated |
| Premium Status | ✅ Shared Field | Both can read |
| Real-time Sync | ❌ Not Enabled | Could be added |
| API Endpoints | Different | Android direct, Web via Express |

## 🎯 Recommendations

1. **Keep using same Supabase project** - Already set up correctly
2. **Run the migration** - Adds premium and attachments
3. **Test cross-platform** - Verify notes sync properly
4. **Consider adding attachments to web** - Optional but nice feature
5. **Document any schema changes** - Keep both platforms in sync

---

The Android app is designed to work seamlessly with your existing backend while adding mobile-specific features (file attachments) in a non-breaking way.


