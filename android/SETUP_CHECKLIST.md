# Android Client - Setup Checklist ✓

Use this checklist to ensure your Android client is properly configured.

## Prerequisites ✓

- [ ] Android Studio Narwhal 4 Feature Drop | 2025.1.4 (or newer) installed
- [ ] JDK 17 installed
- [ ] Supabase account and project created
- [ ] Node.js backend is working (web app runs successfully)

## Supabase Backend Setup ✓

### Database Schema

- [ ] Run `schema.sql` in Supabase SQL Editor
- [ ] Run `migrations/2025-10-15_add_user_id_to_notes.sql`
- [ ] Run `migrations/2025-10-18_add_premium_and_attachments.sql`
- [ ] Verify tables exist: `notes`, `users`, `attachments`
- [ ] Verify `users` table has `is_premium` column

### Authentication

- [ ] Supabase Auth is enabled
- [ ] Email provider is enabled in Auth settings
- [ ] Email confirmation is disabled (or you handle it)
- [ ] Test: Can create user via Supabase dashboard

### Storage

- [ ] Storage bucket `note-attachments` created
- [ ] Bucket is set to public
- [ ] Upload policy created for authenticated users
- [ ] Read policy created for public access

**Quick SQL for storage policies:**
```sql
-- Allow authenticated uploads
CREATE POLICY "Allow authenticated uploads" ON storage.objects
FOR INSERT TO authenticated
WITH CHECK (bucket_id = 'note-attachments');

-- Allow public reads
CREATE POLICY "Allow public reads" ON storage.objects
FOR SELECT TO public
USING (bucket_id = 'note-attachments');

-- Allow authenticated deletes
CREATE POLICY "Allow authenticated deletes" ON storage.objects
FOR DELETE TO authenticated
USING (bucket_id = 'note-attachments');
```

## Android Project Configuration ✓

### Configuration Files

- [ ] `android/local.properties` exists
- [ ] `sdk.dir` is set (Android Studio usually does this automatically)
- [ ] `supabase.url` is set to your Supabase project URL
- [ ] `supabase.anon.key` is set to your Supabase anon key
- [ ] No extra spaces or quotes around values

**Example local.properties:**
```properties
sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
supabase.url=https://abcdefgh.supabase.co
supabase.anon.key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Android Studio

- [ ] Android project opens without errors
- [ ] Gradle sync completes successfully
- [ ] No missing SDK errors
- [ ] Build variant is set to "debug"

### Dependencies

After Gradle sync, verify these are downloaded:
- [ ] Supabase Kotlin SDK (2.6.1)
- [ ] Ktor Client (2.3.12)
- [ ] Material Design 3 libraries
- [ ] AndroidX libraries

## Build and Run ✓

### Initial Build

- [ ] Build → Clean Project
- [ ] Build → Rebuild Project
- [ ] No build errors appear
- [ ] APK is generated successfully

### Device/Emulator

- [ ] Android device connected with USB debugging enabled, OR
- [ ] Android emulator created (recommended: Pixel 6, API 34)
- [ ] Device/emulator appears in target device dropdown
- [ ] Click Run ▶ button
- [ ] App installs successfully
- [ ] App opens without crashing

## Functional Testing ✓

### Authentication

- [ ] Sign Up screen loads
- [ ] Can create new account with email/password
- [ ] Receives proper error for invalid email
- [ ] Receives proper error for short password
- [ ] Sign up successful redirects to main screen
- [ ] Can sign out
- [ ] Can sign in with existing account

### Notes CRUD

- [ ] Empty state message appears when no notes
- [ ] Can click + FAB to create new note
- [ ] Can enter title and content
- [ ] Can save note
- [ ] Note appears in list after save
- [ ] Can click note to open/edit
- [ ] Can update note content
- [ ] Changes persist after save
- [ ] Can delete note with confirmation
- [ ] Pull to refresh works

### File Attachments

- [ ] "Attach File" button is disabled on new notes
- [ ] After saving note, "Attach File" button is enabled
- [ ] Can click "Attach File" and file picker opens
- [ ] Can select file from device
- [ ] File uploads successfully
- [ ] File appears in attachments list
- [ ] Can delete attachment with confirmation
- [ ] Attachments are deleted when note is deleted

### Premium Status

- [ ] Premium status card appears on main screen
- [ ] Shows "Free User" by default
- [ ] After running SQL update, shows "Premium User"

**SQL to test premium:**
```sql
UPDATE users SET is_premium = true WHERE email = 'your-test-email@example.com';
```

### UI/UX

- [ ] Light theme works
- [ ] Dark theme works (enable in device settings)
- [ ] All screens are responsive
- [ ] Toolbar navigation works
- [ ] Loading indicators appear
- [ ] Error messages are user-friendly
- [ ] Dialogs work correctly

## Troubleshooting ✓

If something doesn't work, check:

### Build Errors

**"Supabase URL is not configured"**
- [ ] Verified `local.properties` has correct `supabase.url`
- [ ] No extra spaces or quotes
- [ ] Gradle synced after adding values

**"BuildConfig cannot be resolved"**
- [ ] Cleaned and rebuilt project
- [ ] Verified `buildConfig = true` in build.gradle.kts
- [ ] Tried File → Invalidate Caches / Restart

**"SDK not found"**
- [ ] Opened SDK Manager and installed required SDKs
- [ ] Installed Android SDK Platform 35
- [ ] Installed Build Tools

### Runtime Errors

**App crashes on launch**
- [ ] Checked Logcat for error messages
- [ ] Verified Supabase credentials are correct
- [ ] Verified Supabase project is active

**Authentication fails**
- [ ] Checked Supabase Auth is enabled
- [ ] Verified email provider is enabled
- [ ] Checked Supabase logs for errors

**Notes don't load**
- [ ] Verified database schema is set up
- [ ] Checked RLS policies allow reading
- [ ] Verified user is authenticated

**File upload fails**
- [ ] Verified `note-attachments` bucket exists
- [ ] Checked storage policies
- [ ] Verified internet connection

## Final Verification ✓

- [ ] Can create an account
- [ ] Can create a note
- [ ] Can edit a note
- [ ] Can delete a note
- [ ] Can upload a file
- [ ] Can delete a file
- [ ] Premium status displays correctly
- [ ] Can sign out and back in
- [ ] All data persists across sessions

## Deployment Ready ✓

For production builds:
- [ ] Generated signing key
- [ ] Created keystore.properties
- [ ] Updated build.gradle.kts with signing config
- [ ] Built release APK successfully
- [ ] Tested release APK on device
- [ ] App icon is set
- [ ] App name is correct

## Documentation ✓

- [ ] Read `README.md` for overview
- [ ] Read `QUICKSTART.md` for 5-minute setup
- [ ] Read `ANDROID_SETUP_GUIDE.md` for detailed info
- [ ] Understand project architecture
- [ ] Know where to find help

## Success! 🎉

If all checkboxes are checked, your Android client is fully set up and ready to use!

## Next Steps

1. **Test thoroughly**: Try all features with different scenarios
2. **Customize**: Update colors, theme, app icon
3. **Extend**: Add new features as needed
4. **Deploy**: Build release APK for distribution

## Support

Need help?
- Check `ANDROID_SETUP_GUIDE.md` for detailed troubleshooting
- Review Logcat for runtime errors
- Check Supabase dashboard for backend issues
- Verify all configuration values

---

**Setup Time: ~15 minutes for complete setup and testing**

