# Quick Setup Guide for Notebook Android App

This guide will get you up and running in under 10 minutes.

## ⚡ Quick Start

### 1. Install Android Studio (if not already installed)

Download from: https://developer.android.com/studio

You need: **Android Studio Narwhal 4 Feature Drop | 2025.1.4** or later

### 2. Configure Supabase

#### A. Run Database Migration

1. Open your Supabase dashboard
2. Go to SQL Editor
3. Open the file `../migrations/2025-10-18_add_premium_and_attachments.sql`
4. Copy and paste the content into the SQL Editor
5. Click **Run**

#### B. Create Storage Bucket

1. In Supabase dashboard, go to **Storage**
2. Click **New bucket**
3. Name: `note-attachments`
4. Make it **Public**
5. Click **Create bucket**

#### C. Get Your Credentials

1. Go to **Settings → API**
2. Copy:
   - Project URL (looks like: `https://xxxxx.supabase.co`)
   - Anon key (long string)

### 3. Configure Android App

1. Open the Android project in Android Studio:
   - File → Open
   - Select the `android` folder

2. Wait for Gradle sync to complete (first time may take 5-10 minutes)

3. Edit the configuration file:
   - Open: `app/src/main/java/com/notebook/app/data/SupabaseClient.kt`
   - Replace `SUPABASE_URL` with your Project URL
   - Replace `SUPABASE_ANON_KEY` with your Anon key

```kotlin
private const val SUPABASE_URL = "https://your-project-id.supabase.co"
private const val SUPABASE_ANON_KEY = "eyJhbGc..."  // Your actual anon key
```

4. Save the file

### 4. Run the App

1. Connect Android device (enable USB debugging) OR start emulator
2. Click green **Run** button in Android Studio
3. Wait for build and installation
4. App will launch automatically

### 5. Test the App

1. **Sign Up**: Create a new account with email/password
2. **Create Note**: Tap the + button, enter title and content
3. **Add Attachment**: Save note first, then tap "Attach File"
4. **View Notes**: Pull down to refresh
5. **Edit Note**: Tap on any note
6. **Delete Note**: Open note, tap trash icon

## 🎯 Testing Premium Status

By default, new users are not premium. To test premium features:

1. Sign up/in to the app (note your user ID from logs)
2. In Supabase dashboard, go to **Table Editor → users**
3. Find your user row
4. Set `is_premium` to `true`
5. Pull down to refresh in the app

## 🔧 Common Issues

### "Cannot resolve symbol" errors
- **Solution**: File → Invalidate Caches / Restart

### App crashes immediately
- **Solution**: Check Supabase credentials in SupabaseClient.kt

### Files won't upload
- **Solution**: Ensure storage bucket `note-attachments` exists and is public

### "Network error" on sign in
- **Solution**: 
  1. Check internet connection
  2. Verify Supabase URL is correct
  3. Make sure emulator/device has internet access

## 📱 System Requirements

- **Development**: 
  - Android Studio Narwhal 4 Feature Drop | 2025.1.4+
  - JDK 17+
  - 8GB RAM minimum (16GB recommended)

- **Runtime**:
  - Android 7.0 (API 24) or higher
  - Internet connection required

## 🎨 Optional: Customize the App

### Change App Name
Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">My Notes</string>
```

### Change App Icon
Replace icons in:
- `app/src/main/res/mipmap-*/ic_launcher.png`

### Change Theme Colors
Edit `app/src/main/res/values/colors.xml`

## 📚 Next Steps

- Read the full [README.md](./README.md) for detailed documentation
- Review the code structure and architecture
- Customize the UI to match your brand
- Add additional features as needed

## 💡 Tips

1. **Use emulator for testing**: Pixel 6 Pro API 34 is recommended
2. **Enable auto-import**: Settings → Editor → Auto Import → Optimize imports on the fly
3. **Use Logcat**: View → Tool Windows → Logcat for debugging
4. **Hot reload**: Changes to layouts can be applied without rebuilding

## 🆘 Getting Help

If you encounter issues:
1. Check Logcat for error messages
2. Verify all setup steps were completed
3. Ensure Supabase credentials are correct
4. Check that database migration ran successfully
5. Verify storage bucket exists

Happy coding! 🚀


