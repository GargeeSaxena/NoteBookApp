# CREATE ATTACHMENTS TABLE - RUN THIS NOW!

## Step 1: Open Supabase SQL Editor
1. Go to https://supabase.com/dashboard
2. Select your project
3. Click **SQL Editor** in the left sidebar
4. Click **New query**

## Step 2: Copy and Paste This ENTIRE SQL

```sql
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
```

## Step 3: Run It
1. Click the **RUN** button (or press F5)
2. You should see "Success. No rows returned"

## Step 4: Verify
1. Click **Table Editor** in the left sidebar
2. You should now see an **attachments** table listed

---

# THEN TEST THE APP AGAIN!

After running this SQL:
1. Close and reopen your NoteBook app
2. Try uploading an attachment
3. It should work now!

