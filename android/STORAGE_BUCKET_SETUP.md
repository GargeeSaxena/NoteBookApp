# Storage Bucket Setup - Fix "Bucket not found" Error

## The Error

You're seeing: **"Bucket not found"** when trying to attach files to notes.

This means the storage bucket `note-attachments` doesn't exist in your Supabase project yet.

## Quick Fix (5 minutes)

### Step 1: Go to Supabase Dashboard

1. Open https://supabase.com/dashboard
2. Select your project (mkxytkpewzmaucgwhahs)

### Step 2: Create the Storage Bucket

1. In the left sidebar, click **Storage**
2. Click the **New Bucket** button (or **Create a new bucket**)
3. Fill in the details:
   - **Name**: `note-attachments` (exactly this, no spaces!)
   - **Public bucket**: ✅ **Check this box** (make it public)
   - Click **Create bucket**

### Step 3: Set Up Storage Policies

After creating the bucket, you need to add policies so users can upload/read/delete files.

#### Option A: Use the SQL Editor (Easiest)

1. Click **SQL Editor** in the left sidebar
2. Click **New query**
3. Copy and paste this SQL:

```sql
-- Enable RLS on storage.objects
ALTER TABLE storage.objects ENABLE ROW LEVEL SECURITY;

-- Allow authenticated users to upload files
CREATE POLICY "Allow authenticated uploads"
ON storage.objects
FOR INSERT
TO authenticated
WITH CHECK (bucket_id = 'note-attachments');

-- Allow public reads
CREATE POLICY "Allow public reads"
ON storage.objects
FOR SELECT
TO public
USING (bucket_id = 'note-attachments');

-- Allow authenticated users to update
CREATE POLICY "Allow authenticated updates"
ON storage.objects
FOR UPDATE
TO authenticated
USING (bucket_id = 'note-attachments')
WITH CHECK (bucket_id = 'note-attachments');

-- Allow authenticated users to delete
CREATE POLICY "Allow authenticated deletes"
ON storage.objects
FOR DELETE
TO authenticated
USING (bucket_id = 'note-attachments');
```

4. Click **Run** (or press Ctrl+Enter)
5. You should see "Success. No rows returned"

#### Option B: Use Storage Policies UI

1. In **Storage**, click on your **note-attachments** bucket
2. Click the **Policies** tab
3. Click **New policy**

**For Uploads:**
- Policy name: `Allow authenticated uploads`
- Allowed operation: `INSERT`
- Target roles: `authenticated`
- USING expression: `true`
- WITH CHECK expression: `true`
- Click **Create policy**

**For Reads:**
- Policy name: `Allow public reads`
- Allowed operation: `SELECT`
- Target roles: `public`
- USING expression: `true`
- Click **Create policy**

**For Deletes:**
- Policy name: `Allow authenticated deletes`
- Allowed operation: `DELETE`
- Target roles: `authenticated`
- USING expression: `true`
- Click **Create policy**

### Step 4: Test in Your App

1. **Rebuild your app** in Android Studio:
   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

2. **Uninstall the app** from your device/emulator:
   - Long press the Notebook icon
   - Tap Uninstall

3. **Run the app** again from Android Studio

4. **Sign up/Sign in**

5. **Create a note and save it**

6. **Click "Attach File"** - it should work now! ✅

---

## Verify the Bucket Exists

To make sure the bucket was created correctly:

1. Go to **Storage** in Supabase dashboard
2. You should see `note-attachments` in the list
3. It should show as **Public**
4. Click on it - you should be able to see the Policies tab

---

## What Files Can Be Attached?

The app supports **all file types**:
- Images (JPG, PNG, GIF)
- Documents (PDF, DOC, TXT)
- Videos (MP4, MOV)
- Audio (MP3, WAV)
- Any other file!

---

## Summary of Changes Needed:

✅ **Create bucket**: `note-attachments` (public)  
✅ **Add upload policy**: Allow authenticated users to upload  
✅ **Add read policy**: Allow public reads  
✅ **Add delete policy**: Allow authenticated users to delete  
✅ **Rebuild app**: Clean and rebuild in Android Studio  
✅ **Reinstall app**: Uninstall and run again  

After these steps, file attachments will work perfectly! 🎉

---

## Troubleshooting

**Still getting "Bucket not found"?**
- Make sure the bucket name is exactly `note-attachments` (with the hyphen)
- Make sure it's marked as public
- Try rebuilding and reinstalling the app

**Can't create policies?**
- Use the SQL Editor method (Option A above)
- Make sure you're logged into the correct Supabase project

**Files upload but can't see them?**
- Check the read policy is set to `public` or `authenticated`
- Verify the policy is enabled in Storage → Policies tab

