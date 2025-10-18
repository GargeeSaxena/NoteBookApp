# Android Client - 5 Minute Setup

Get the Android client running in 5 minutes!

## Prerequisites

- ✅ Android Studio Narwhal 4 or newer
- ✅ Supabase project with database schema set up
- ✅ Storage bucket `note-attachments` created in Supabase

## Setup Steps

### 1. Configure Supabase (2 minutes)

```bash
cd android
cp local.properties.example local.properties
```

Edit `local.properties`:
```properties
supabase.url=https://YOUR-PROJECT-ID.supabase.co
supabase.anon.key=YOUR-ANON-KEY-HERE
```

**Where to find these values:**
- Supabase Dashboard → Project Settings → API
- Copy "Project URL" and "anon/public key"

### 2. Open in Android Studio (1 minute)

1. Open Android Studio
2. File → Open → Select `android` folder
3. Wait for Gradle sync (first time may take 2-3 minutes)

### 3. Create Storage Bucket (1 minute)

In Supabase Dashboard:
1. Go to Storage
2. Create new bucket: `note-attachments`
3. Make it public
4. Add these policies:
   ```sql
   -- Allow authenticated users to upload
   CREATE POLICY "Allow authenticated uploads" ON storage.objects
   FOR INSERT TO authenticated
   WITH CHECK (bucket_id = 'note-attachments');
   
   -- Allow public reads
   CREATE POLICY "Allow public reads" ON storage.objects
   FOR SELECT TO public
   USING (bucket_id = 'note-attachments');
   ```

### 4. Run the App (1 minute)

1. Connect Android device or start emulator
2. Click green ▶ Run button
3. Wait for app to install
4. Sign up with an email/password

## That's It! 🎉

You now have a fully functional Android notes app with:
- ✅ Authentication
- ✅ CRUD operations
- ✅ File uploads
- ✅ Premium status display

## First Steps

1. **Sign Up**: Create an account
2. **Add Note**: Click the + button
3. **Upload File**: Save a note first, then attach a file
4. **Check Status**: See your premium status at the top

## Troubleshooting

### App won't build?
```bash
# Verify local.properties has correct values
cat local.properties

# Sync Gradle
# In Android Studio: File → Sync Project with Gradle Files
```

### App crashes on launch?
- Check Supabase URL and key are correct
- Verify Supabase project is active
- Check Auth is enabled in Supabase

### Can't upload files?
- Make sure `note-attachments` bucket exists
- Check storage policies allow uploads

## Next Steps

- Read [ANDROID_SETUP_GUIDE.md](./ANDROID_SETUP_GUIDE.md) for detailed documentation
- Test premium status by updating database:
  ```sql
  UPDATE users SET is_premium = true WHERE email = 'your@email.com';
  ```

## Need Help?

1. Check [ANDROID_SETUP_GUIDE.md](./ANDROID_SETUP_GUIDE.md)
2. Review Android Studio Logcat for errors
3. Check Supabase dashboard for backend issues

---

**Time to build: ~5 minutes**  
**First note: < 1 minute** ⏱️

