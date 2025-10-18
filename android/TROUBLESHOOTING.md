# Troubleshooting Guide

## ✅ FIXED: Kotlin 2.0 Compose Compiler Error

### The Error
```
com.android.builder.errors.EvalIssueException: Starting in Kotlin 2.0, 
the Compose Compiler Gradle plugin is required when compose is enabled.
```

### The Fix (Applied)

I've updated your build files to add the Compose Compiler plugin. The following files have been fixed:

1. **android/build.gradle.kts** - Added compose plugin declaration
2. **android/app/build.gradle.kts** - Applied compose plugin

## ✅ FIXED: debugRuntimeClasspathCopy Configuration Error

### The Error
```
Cannot select root node 'debugRuntimeClasspathCopy' as a variant. 
Configurations should not act as both a resolution root and a variant simultaneously.
```

### The Fix (Applied)

This was caused by Android Gradle Plugin 8.7.3 compatibility issues. I've downgraded to stable versions:

**Changes made:**
- Android Gradle Plugin: 8.7.3 → 8.5.2
- Kotlin: 2.1.0 → 2.0.20
- Supabase BOM: 3.0.2 → 2.6.1
- Ktor: 3.0.1 → 2.3.12

These are proven stable versions that work together perfectly.

## ✅ FIXED: Missing Launcher Icon Resources

### The Error
```
error: resource mipmap/ic_launcher_foreground not found.
Android resource linking failed
```

### The Fix (Applied)

The app icon files were incomplete. I've fixed this by:

1. **Removed incomplete adaptive icon XMLs**
2. **Set to use Android's default icon** (temporary)
3. **Updated AndroidManifest.xml** to use system icon

**To add your custom icon:**
1. In Android Studio: Right-click `app/src/main/res`
2. Select **New → Image Asset**
3. Upload your icon (512x512 PNG)
4. Click **Finish**

Android Studio will generate all required icon files automatically.

## 🔴 CRITICAL: Windows File Lock Error (AccessDeniedException)

### The Error
```
java.nio.file.AccessDeniedException: 
C:\...\android\app\build\intermediates\...
```

### Why It Happens
- **OneDrive** is syncing and locking build files
- **Gradle daemon** hasn't released file handles  
- **Windows Defender** or antivirus is scanning files
- **Android Studio** hasn't closed properly

### The Fix (2 minutes)

**1. Close Android Studio COMPLETELY**

**2. Open PowerShell and run:**
```powershell
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"
.\gradlew --stop
Remove-Item -Recurse -Force .\app\build -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\build -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\.gradle -ErrorAction SilentlyContinue
```

**3. If files are still locked:**
- Right-click **OneDrive icon** in system tray
- Click **Pause syncing → 2 hours**
- Run the delete commands again

**4. Reopen Android Studio:**
- **File → Invalidate Caches / Restart**
- **File → Sync Project with Gradle Files**
- **Build → Clean Project**
- **Build → Rebuild Project**

**5. If still fails:** Restart your computer

### Permanent Fix
Move project out of OneDrive to: `C:\AndroidProjects\NoteBook App\`

---

### What to Do Now

**Option 1: Use the Fixed Version (Recommended)**

Simply **sync your Gradle** files in Android Studio:
1. Click **File → Sync Project with Gradle Files**
2. Wait for sync to complete
3. Try building again

**Option 2: If Still Having Issues**

If you continue to have problems, use the simplified version without Compose:

1. Replace `android/app/build.gradle.kts` with `android/app/build.gradle.kts.alternative`
2. In the root `android/build.gradle.kts`, remove this line:
   ```kotlin
   id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
   ```

---

## Common Build Issues

### Issue 1: "SDK not found"
**Error**: SDK location not found
**Fix**: 
1. Go to **File → Project Structure**
2. Set Android SDK location
3. Or create `local.properties` with:
   ```
   sdk.dir=C\:\\Users\\HP\\AppData\\Local\\Android\\Sdk
   ```

### Issue 2: "Internet connection required"
**Error**: Could not resolve dependencies
**Fix**:
1. Check internet connection
2. Try **File → Invalidate Caches / Restart**
3. Or sync again

### Issue 3: "Gradle version mismatch"
**Error**: Unsupported Gradle version
**Fix**:
1. Android Studio will prompt to update
2. Click "Update" when asked
3. Or go to **File → Project Structure → Project** and set Gradle version

### Issue 4: "Kotlin version conflict"
**Error**: Kotlin version conflicts
**Fix**: All versions should match:
- Root build.gradle.kts: `2.1.0`
- App build.gradle.kts: Uses root version
- Compose plugin: `2.1.0`

### Issue 5: "Build tools not installed"
**Error**: Failed to find Build Tools revision X.X.X
**Fix**:
1. Go to **Tools → SDK Manager**
2. Click **SDK Tools** tab
3. Check **Android SDK Build-Tools 35**
4. Click **Apply**

---

## Quick Fixes Checklist

When encountering build errors, try these in order:

- [ ] **Sync Gradle**: File → Sync Project with Gradle Files
- [ ] **Clean Build**: Build → Clean Project, then Build → Rebuild Project
- [ ] **Invalidate Caches**: File → Invalidate Caches / Restart
- [ ] **Check Internet**: Ensure stable internet connection
- [ ] **Update Android Studio**: Help → Check for Updates
- [ ] **Check SDK**: Tools → SDK Manager (ensure SDK 35 is installed)
- [ ] **Check JDK**: File → Project Structure → SDK Location (should be JDK 17)

---

## Specific Error Solutions

### "Cannot resolve symbol 'R'"
1. Clean and rebuild project
2. Check `namespace = "com.notebook.app"` in build.gradle.kts
3. Sync Gradle files

### "Manifest merger failed"
1. Check AndroidManifest.xml for errors
2. Ensure all `<activity>` tags are properly closed
3. Remove duplicate declarations

### "Execution failed for task ':app:compileDebugKotlin'"
1. Check for Kotlin syntax errors in .kt files
2. Ensure all imports are correct
3. Clean and rebuild

### "Supabase dependencies not found"
1. Ensure `mavenCentral()` is in repositories
2. Check internet connection
3. Sync Gradle files
4. Try offline mode: Settings → Build → Gradle → Offline work (uncheck)

---

## Runtime Issues

### App crashes on launch
**Possible causes:**
1. Supabase credentials not configured
2. Internet permission missing
3. Activity not declared in manifest

**Fix:**
1. Check `SupabaseClient.kt` has correct URL and key
2. Verify AndroidManifest.xml has internet permission
3. Check Logcat for error details

### "Network error" when signing in
**Fix:**
1. Verify Supabase URL is correct
2. Check device/emulator has internet
3. Verify Supabase anon key is valid
4. Check Supabase project is active

### Notes not loading
**Fix:**
1. Ensure user is signed in
2. Check database schema is correct
3. Verify RLS policies in Supabase
4. Check Logcat for API errors

### File upload fails
**Fix:**
1. Ensure `note-attachments` bucket exists
2. Verify bucket is public or has correct policies
3. Check note is saved (noteId is not null)
4. Verify storage permissions on device

---

## Logcat Debugging

To view detailed errors:

1. Open Logcat: **View → Tool Windows → Logcat**
2. Select your device/emulator
3. Filter by package: `com.notebook.app`
4. Look for red error lines
5. Common error tags:
   - `AndroidRuntime` - Crashes
   - `System.err` - Exceptions
   - `Supabase` - API errors

---

## Gradle Sync Issues

### "Plugin not found"
**Fix**: Check plugin versions in root build.gradle.kts:
```kotlin
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}
```

### "Could not find method implementation()"
**Fix**: Ensure you're editing app/build.gradle.kts, not root build.gradle.kts

### "Duplicate class found"
**Fix**: Check for duplicate dependencies in build.gradle.kts

---

## Getting More Help

### Check Documentation
1. [SETUP_GUIDE.md](./SETUP_GUIDE.md) - Setup instructions
2. [README.md](./README.md) - Full documentation
3. [FEATURES.md](./FEATURES.md) - Feature details

### Android Studio Help
- **Help → Find Action** (Ctrl+Shift+A / Cmd+Shift+A)
- Search for any action or setting

### External Resources
- [Android Developer Docs](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Supabase Kotlin Docs](https://github.com/supabase-community/supabase-kt)

---

## Still Stuck?

If none of these solutions work:

1. **Copy the complete error message** from Logcat or Build output
2. **Check which file** is causing the issue
3. **Try the alternative build file** (without Compose)
4. **Update Android Studio** to the latest version
5. **Create a new project** and copy code files over

## Prevention Tips

- Always sync Gradle after changing build files
- Keep Android Studio updated
- Keep SDK and build tools updated
- Don't modify generated files (like R.java)
- Use stable dependency versions
- Test on real device when possible

---

**Last Updated**: 2025-10-18

This guide will be updated as new issues are discovered and resolved.

