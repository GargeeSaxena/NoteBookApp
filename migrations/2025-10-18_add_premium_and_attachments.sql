-- Migration to add premium status field to users table
-- and create attachments table for file storage

-- Add is_premium column to users table
alter table if exists public.users
  add column if not exists is_premium boolean not null default false;

-- Create attachments table for file storage
create table if not exists public.attachments (
    id uuid primary key default gen_random_uuid(),
    note_id uuid not null references public.notes(id) on delete cascade,
    file_name text not null,
    file_path text not null,
    file_size bigint not null,
    mime_type text,
    created_at timestamptz not null default now()
);

-- Index for faster lookups by note_id
create index if not exists idx_attachments_note_id on public.attachments (note_id);

-- Row Level Security for attachments
alter table public.attachments enable row level security;

drop policy if exists "Enable read for all users" on public.attachments;
create policy "Enable read" on public.attachments for select using (true);

drop policy if exists "Enable insert for all users" on public.attachments;
create policy "Enable insert" on public.attachments for insert with check (true);

drop policy if exists "Enable delete for all users" on public.attachments;
create policy "Enable delete" on public.attachments for delete using (true);

-- Update notes table to allow deletion of notes with attachments
-- (already handled by the on delete cascade on attachments table)


