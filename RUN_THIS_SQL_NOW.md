# 🚀 Run This SQL in Supabase NOW

## Copy and paste this EXACT code into Supabase SQL Editor:

```sql
-- Step 1: Create the storage bucket
INSERT INTO storage.buckets (id, name, public)
VALUES ('note-attachments', 'note-attachments', true)
ON CONFLICT (id) DO NOTHING;

-- Step 2: Enable RLS on storage.objects (if not already enabled)
ALTER TABLE storage.objects ENABLE ROW LEVEL SECURITY;

-- Step 3: Allow authenticated users to upload files
CREATE POLICY "Allow authenticated uploads"
ON storage.objects
FOR INSERT
TO authenticated
WITH CHECK (bucket_id = 'note-attachments');

-- Step 4: Allow public reads
CREATE POLICY "Allow public reads"
ON storage.objects
FOR SELECT
TO public
USING (bucket_id = 'note-attachments');

-- Step 5: Allow authenticated users to update
CREATE POLICY "Allow authenticated updates"
ON storage.objects
FOR UPDATE
TO authenticated
USING (bucket_id = 'note-attachments')
WITH CHECK (bucket_id = 'note-attachments');

-- Step 6: Allow authenticated users to delete
CREATE POLICY "Allow authenticated deletes"
ON storage.objects
FOR DELETE
TO authenticated
USING (bucket_id = 'note-attachments');

-- Step 7: Verify the bucket was created
SELECT id, name, public FROM storage.buckets WHERE id = 'note-attachments';
```

## How to Run:

1. Go to https://supabase.com/dashboard
2. Select your project
3. Click **SQL Editor** on the left
4. Click **New query**
5. **Copy ALL the SQL above**
6. **Paste it** into the SQL editor
7. Click **RUN** (or press Ctrl+Enter)

## Expected Result:

You should see:
```
Success. 1 row(s) returned
```

And a table showing:
- id: note-attachments
- name: note-attachments
- public: true

## If You Get Errors:

### Error: "policy already exists"
✅ **This is OK!** It means the policy was already created before. Just continue - the bucket still works!

### Error: "bucket already exists"
✅ **This is OK!** The bucket already exists. Just continue with the rest of the SQL.

### Error: "permission denied"
❌ **This is a problem.** Make sure you're logged into the correct Supabase project.

## After Running:

1. Click **Storage** in the left sidebar
2. You should see **note-attachments** bucket
3. It should show as **Public**

✅ **Done! Now file attachments will work in your app!**

## Next Step:

1. **Rebuild your Android app** in Android Studio
2. **Uninstall the old version** from your phone
3. **Run the app** again
4. **Sign up/Sign in**
5. **Create a note** and **attach a file** - it will work! 🎉

