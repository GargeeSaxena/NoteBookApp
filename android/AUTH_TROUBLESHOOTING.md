# Authentication Troubleshooting

## Issue: Not Getting Authentication Page

If the app is not showing the login/signup screen, it's likely because:
1. Old session data is cached in SharedPreferences
2. The app thinks you're logged in but the session is invalid

## Quick Fix: Clear App Data

### Method 1: Through Android Settings (Recommended)
1. Go to your device **Settings**
2. Go to **Apps** or **Application Manager**
3. Find and tap **Notebook**
4. Tap **Storage**
5. Tap **Clear Data** or **Clear Storage**
6. Tap **OK** to confirm
7. Reopen the app - you'll see the login screen!

### Method 2: Uninstall and Reinstall
1. Long press the Notebook app icon
2. Tap **Uninstall** or drag to uninstall
3. In Android Studio, click Run ▶️ to reinstall
4. The app will open fresh with the login screen

### Method 3: Through Android Studio
1. In Android Studio, go to **Run** menu
2. Select **Run** → **Edit Configurations**
3. Under **Miscellaneous**, check **Clear application data on device before launch**
4. Click **OK**
5. Run the app again

---

## After Clearing Data

1. You'll see the **Sign Up / Sign In** screen
2. Enter email: `test@example.com`
3. Enter password: `test123` (minimum 6 characters)
4. Tap **Sign Up** to create account
5. Create your first note!

---

## Understanding the Fix

### What Was Wrong:
1. **JSON Parsing Error**: Supabase operations weren't calling `.select()`, causing empty responses
2. **Cached Auth**: Old user ID was stored in SharedPreferences from a broken session

### What Was Fixed:
✅ Added `.select()` to all Supabase insert/update operations  
✅ Improved authentication validation  
✅ Added Kotlin serialization plugin  

---

## Testing After Fix

1. **Clear app data** (using Method 1 above)
2. **Rebuild the app** in Android Studio:
   - File → Sync Project with Gradle Files
   - Build → Clean Project
   - Build → Rebuild Project
3. **Run the app**
4. **Sign up** with email/password
5. **Create a note** - it should save successfully! ✅

---

## If You Still Have Issues

Make sure you:
- ✅ Synced Gradle files (File → Sync Project with Gradle Files)
- ✅ Rebuilt the project (Build → Rebuild Project)
- ✅ Cleared app data before testing
- ✅ Have correct Supabase credentials in `local.properties`

The note saving error is now fixed - just clear the app data and rebuild!

