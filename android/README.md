# Notebook App - Android Client

A simple and elegant Android client for the Notebook App with note-taking and file attachment capabilities.

## Features

- ✅ Email/Password Authentication via Supabase
- ✅ Create, Read, Update, Delete (CRUD) notes
- ✅ File upload and storage via Supabase Storage
- ✅ Premium status display
- ✅ Material Design 3 UI
- ✅ Dark theme support
- ✅ Modern Android architecture (MVVM)

## Quick Start

### 1. Prerequisites

- Android Studio Narwhal 4 Feature Drop | 2025.1.4 or newer
- JDK 17
- A Supabase project configured with the database schema

### 2. Setup

1. **Create local.properties from the example**:
   ```bash
   cp local.properties.example local.properties
   ```

2. **Add your Supabase credentials** to `local.properties`:
   ```properties
   supabase.url=https://your-project-id.supabase.co
   supabase.anon.key=your-anon-key-here
   ```

3. **Open the android folder in Android Studio**

4. **Wait for Gradle sync to complete**

5. **Run the app** on an emulator or physical device

## Supabase Setup

Make sure your Supabase project has:

1. **Auth enabled** with Email provider
2. **Database tables**: `notes`, `users`, `attachments`
3. **Storage bucket**: `note-attachments` (public bucket)

Run the SQL migrations from the project root:
- `schema.sql`
- `migrations/2025-10-15_add_user_id_to_notes.sql`
- `migrations/2025-10-18_add_premium_and_attachments.sql`

## Documentation

For detailed setup instructions, troubleshooting, and architecture details, see:
- **[ANDROID_SETUP_GUIDE.md](./ANDROID_SETUP_GUIDE.md)** - Comprehensive setup and usage guide

## Tech Stack

- **Language**: Kotlin
- **UI**: Material Design 3, XML layouts, ViewBinding
- **Architecture**: MVVM
- **Backend**: Supabase (Auth, Database, Storage)
- **Async**: Kotlin Coroutines
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)

## Project Structure

```
app/src/main/
├── java/com/notebook/app/
│   ├── data/              # Data layer (models, repositories, Supabase client)
│   ├── ui/                # UI layer (activities, adapters)
│   ├── viewmodel/         # ViewModels for MVVM
│   └── NotebookApplication.kt
└── res/                   # Resources (layouts, strings, themes, etc.)
```

## What's NOT Implemented

As per requirements:
- ❌ DodoPayments integration
- ❌ In-app purchases
- ❌ Offline mode
- ❌ Rich text editing

## Support

For issues or questions:
1. Check [ANDROID_SETUP_GUIDE.md](./ANDROID_SETUP_GUIDE.md)
2. Review Android Studio Logcat for runtime errors
3. Check Supabase dashboard for backend errors

## License

MIT
