-- Fix storage policies for note-attachments bucket
-- Run this in Supabase SQL Editor to fix the RLS error

-- Drop all existing policies on storage.objects for note-attachments bucket
DROP POLICY IF EXISTS "Allow authenticated uploads" ON storage.objects;
DROP POLICY IF EXISTS "Allow public reads" ON storage.objects;
DROP POLICY IF EXISTS "Allow authenticated updates" ON storage.objects;
DROP POLICY IF EXISTS "Allow authenticated deletes" ON storage.objects;
DROP POLICY IF EXISTS "Allow all operations" ON storage.objects;
DROP POLICY IF EXISTS "Give users authenticated access to folder" ON storage.objects;

-- Create simple policies that allow all operations for authenticated users
CREATE POLICY "authenticated_all_access"
ON storage.objects
FOR ALL
TO authenticated
USING (bucket_id = 'note-attachments')
WITH CHECK (bucket_id = 'note-attachments');

-- Allow public to read files
CREATE POLICY "public_read_access"
ON storage.objects
FOR SELECT
TO public
USING (bucket_id = 'note-attachments');

