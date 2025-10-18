-- Create storage bucket for note attachments
-- Run this in Supabase SQL Editor

-- Create the storage bucket
INSERT INTO storage.buckets (id, name, public)
VALUES ('note-attachments', 'note-attachments', true)
ON CONFLICT (id) DO NOTHING;

-- Policy: Allow authenticated users to upload
INSERT INTO storage.policies (name, bucket_id, definition, check_expression)
VALUES (
  'Allow authenticated uploads',
  'note-attachments',
  'bucket_id = ''note-attachments'' AND auth.role() = ''authenticated''',
  'bucket_id = ''note-attachments'' AND auth.role() = ''authenticated'''
)
ON CONFLICT DO NOTHING;

-- Policy: Allow public reads
INSERT INTO storage.policies (name, bucket_id, definition)
VALUES (
  'Allow public reads',
  'note-attachments',
  'bucket_id = ''note-attachments'''
)
ON CONFLICT DO NOTHING;

-- Policy: Allow authenticated users to delete
INSERT INTO storage.policies (name, bucket_id, definition, check_expression)
VALUES (
  'Allow authenticated deletes',
  'note-attachments',
  'bucket_id = ''note-attachments'' AND auth.role() = ''authenticated''',
  'bucket_id = ''note-attachments'' AND auth.role() = ''authenticated'''
)
ON CONFLICT DO NOTHING;

-- Verify the bucket was created
SELECT * FROM storage.buckets WHERE id = 'note-attachments';

