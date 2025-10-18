# Android Client Setup Guide

This guide will help you set up and run the Android client for the Notebook App.

## Prerequisites

- **Android Studio Narwhal 4 Feature Drop | 2025.1.4** (or newer)
- **Java Development Kit (JDK) 17**
- **Android SDK** (automatically installed with Android Studio)
- **A Supabase project** with the database schema set up

## Project Architecture

The Android client uses modern Android development best practices:

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI**: Material Design 3 with XML layouts and View Binding
- **Networking**: Supabase Kotlin Client
- **Async**: Kotlin Coroutines
- **Dependency Injection**: Manual (no DI framework for simplicity)

## Features Implemented

✅ **Authentication**
- Email/Password sign up and sign in via Supabase Auth
- Google OAuth support (requires additional setup)
- Session persistence with SharedPreferences

✅ **CRUD Operations for Notes**
- Create new notes
- Read/list all user notes
- Update existing notes
- Delete notes

✅ **File Attachments**
- Upload files to Supabase Storage
- View attached files
- Delete attachments
- Support for any file type

✅ **Premium Status Display**
- Shows whether user is Premium or Free
- Retrieved from Supabase database

❌ **DodoPayments** (Not Implemented as requested)

## Setup Instructions

### Step 1: Configure Supabase

1. **Create a Supabase Storage Bucket** (if not already created):
   - Go to your Supabase dashboard
   - Navigate to **Storage**
   - Create a new public bucket named `note-attachments`
   - Set appropriate policies (allow authenticated users to upload/read/delete)

2. **Run Database Migrations**:
   
   Make sure you've run these SQL files in your Supabase SQL Editor:
   - `schema.sql` (main schema)
   - `migrations/2025-10-15_add_user_id_to_notes.sql`
   - `migrations/2025-10-18_add_premium_and_attachments.sql`

3. **Get Your Supabase Credentials**:
   - Go to **Project Settings** → **API**
   - Copy your **Project URL** (e.g., `https://your-project-id.supabase.co`)
   - Copy your **anon/public key**

### Step 2: Configure Android Project

1. **Navigate to the Android directory**:
   ```bash
   cd android
   ```

2. **Create local.properties file**:
   ```bash
   # On Windows
   copy local.properties.example local.properties
   
   # On Linux/Mac
   cp local.properties.example local.properties
   ```

3. **Edit local.properties** and add your Supabase credentials:
   ```properties
   sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
   
   supabase.url=https://your-project-id.supabase.co
   supabase.anon.key=your-actual-anon-key-here
   ```
   
   **Note**: The SDK directory is usually set automatically by Android Studio.

### Step 3: Open Project in Android Studio

1. **Open Android Studio**

2. **Open the project**:
   - Click **File** → **Open**
   - Navigate to the `android` folder in your project
   - Click **OK**

3. **Wait for Gradle sync** to complete (this may take a few minutes on first run)

4. **Resolve any SDK/dependency issues**:
   - Android Studio will prompt you to install missing SDKs
   - Follow the prompts to install required components

### Step 4: Run the App

1. **Connect a device or start an emulator**:
   - Physical device: Enable USB debugging and connect via USB
   - Emulator: Click **Device Manager** → **Create Device** (recommended: Pixel 6 with API 34)

2. **Run the app**:
   - Click the **Run** button (green play icon) or press `Shift + F10`
   - Select your target device
   - Wait for the app to build and install

## Usage Guide

### First Launch

1. **Sign Up**: Create a new account with email and password
2. **Sign In**: Use your credentials to sign in

### Creating Notes

1. Click the **Floating Action Button** (+ icon) at the bottom right
2. Enter a **Title** and **Content**
3. Click the **Save** button (disk icon)
4. Your note is created!

### Adding Attachments

1. Open an existing note (you must save the note first)
2. Scroll to the **Attachments** section
3. Click **Attach File**
4. Select a file from your device
5. The file will upload to Supabase Storage

### Editing/Deleting Notes

1. **Edit**: Tap a note to open it, make changes, and save
2. **Delete**: Open a note → tap the delete icon (trash) in the toolbar

### Premium Status

- Your premium status is shown in a card at the top of the main screen
- This is retrieved from the `users` table in Supabase
- The `is_premium` column determines if you're a Premium or Free user

## Project Structure

```
android/
├── app/
│   ├── src/main/
│   │   ├── java/com/notebook/app/
│   │   │   ├── data/
│   │   │   │   ├── model/           # Data classes (Note, User, Attachment)
│   │   │   │   ├── repository/      # Repository layer for data access
│   │   │   │   └── SupabaseClient.kt # Supabase configuration
│   │   │   ├── ui/
│   │   │   │   ├── auth/            # Authentication screens
│   │   │   │   ├── note/            # Note detail screens
│   │   │   │   ├── MainActivity.kt  # Main notes list screen
│   │   │   │   └── NotesAdapter.kt  # RecyclerView adapter
│   │   │   ├── viewmodel/           # ViewModels for MVVM
│   │   │   └── NotebookApplication.kt
│   │   ├── res/
│   │   │   ├── layout/              # XML layouts
│   │   │   ├── values/              # Strings, colors, themes
│   │   │   ├── values-night/        # Dark theme
│   │   │   ├── menu/                # Menu resources
│   │   │   └── drawable/            # Icons and drawables
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts             # App-level build configuration
├── build.gradle.kts                 # Project-level build configuration
├── local.properties                 # Local configuration (DO NOT COMMIT)
└── local.properties.example         # Template for local.properties
```

## Troubleshooting

### Build Errors

**Problem**: `SUPABASE_URL is not configured` or `BuildConfig cannot be resolved`

**Solution**: 
1. Make sure you've created `local.properties` from the example
2. Add your Supabase credentials to `local.properties`
3. Sync Gradle files: **File** → **Sync Project with Gradle Files**
4. Clean and rebuild: **Build** → **Clean Project** → **Build** → **Rebuild Project**

---

**Problem**: SDK not found

**Solution**:
1. Open **Tools** → **SDK Manager**
2. Install **Android SDK Platform 35** (or the version specified in build.gradle.kts)
3. Install **Android SDK Build-Tools**
4. Sync Gradle files

---

**Problem**: Kotlin compiler errors

**Solution**:
1. Check that you're using **JDK 17**
2. Go to **File** → **Project Structure** → **SDK Location**
3. Ensure JDK is set to version 17

### Runtime Errors

**Problem**: App crashes on launch with Supabase initialization error

**Solution**:
1. Verify your Supabase URL and key are correct in `local.properties`
2. Make sure there are no extra spaces or quotes in the configuration
3. Check that your Supabase project is active

---

**Problem**: Authentication fails

**Solution**:
1. Check Supabase Auth is enabled in your project
2. Verify email confirmation is disabled (or handle email verification)
3. Check Supabase logs in the dashboard for errors

---

**Problem**: File upload fails

**Solution**:
1. Make sure the `note-attachments` storage bucket exists
2. Check storage policies allow authenticated users to upload
3. Verify internet connection and network permissions

---

**Problem**: Notes don't load

**Solution**:
1. Check that the database schema is properly set up
2. Verify RLS policies allow reading notes
3. Check network connectivity

### Emulator Issues

**Problem**: Emulator is slow

**Solution**:
1. Enable Hardware Acceleration (HAXM on Windows/Mac, KVM on Linux)
2. Allocate more RAM to the emulator (4GB recommended)
3. Use a recent system image (API 34+)

## Testing Accounts

For development/testing, you can create test accounts directly through the app's Sign Up screen.

**Recommended test accounts**:
- Email: `test@example.com`, Password: `test123`
- Email: `premium@example.com`, Password: `premium123`

Remember to set the `is_premium` field in the database for premium testing:

```sql
UPDATE users SET is_premium = true WHERE email = 'premium@example.com';
```

## Additional Configuration

### Google OAuth (Optional)

Google OAuth requires additional setup:

1. **Firebase Console**:
   - Create a Firebase project (or link your existing one)
   - Enable Google Sign-In in Authentication
   - Download `google-services.json` and place it in `android/app/`

2. **Supabase Configuration**:
   - Go to **Authentication** → **Providers** → **Google**
   - Add your OAuth credentials

3. **Android Configuration**:
   - Add Google Services plugin to `build.gradle.kts`
   - Update `AndroidManifest.xml` with appropriate intent filters

For detailed Google OAuth setup, refer to the Supabase and Firebase documentation.

## Building for Release

When you're ready to build a release APK:

1. **Generate a signing key**:
   ```bash
   keytool -genkey -v -keystore notebook-release-key.keystore -alias notebook -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Create keystore.properties** (in `android/` directory):
   ```properties
   storePassword=your-keystore-password
   keyPassword=your-key-password
   keyAlias=notebook
   storeFile=notebook-release-key.keystore
   ```

3. **Update build.gradle.kts** to use the signing configuration

4. **Build release APK**:
   ```bash
   ./gradlew assembleRelease
   ```

The signed APK will be in `app/build/outputs/apk/release/`

## Contributing

When contributing to the Android client:

1. Follow Kotlin coding conventions
2. Use meaningful variable and function names
3. Add comments for complex logic
4. Test on both light and dark themes
5. Test on different screen sizes
6. Ensure proper error handling

## Support

If you encounter issues:

1. Check the Supabase dashboard for backend errors
2. Use Android Studio's Logcat to view runtime logs
3. Verify all configuration files are set up correctly
4. Refer to the main project documentation

## Version Information

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35
- **Kotlin**: 1.9+
- **Gradle**: 8.9
- **Supabase Kotlin SDK**: 2.6.1

## What's Not Implemented

As per requirements, the following features are **NOT** implemented:

- ❌ DodoPayments integration
- ❌ In-app purchases for premium upgrade
- ❌ Offline sync/caching
- ❌ Rich text editor
- ❌ Note sharing
- ❌ Tags or categories

These could be added in future iterations if needed.

---

**Happy coding! 🚀**

