-- Supabase SQL schema for NoteBook App
-- Run this in Supabase SQL editor.

-- 0) Ensure required extensions are enabled BEFORE creating trigram index
create extension if not exists pgcrypto; -- for gen_random_uuid
create extension if not exists pg_trgm;  -- for gin_trgm_ops

-- Ensure user_id exists for existing deployments
alter table if exists public.notes add column if not exists user_id text not null default '';

-- 1) Notes table
create table if not exists public.notes (
    id uuid primary key default gen_random_uuid(),
    user_id text not null,
    title text not null check (char_length(title) <= 200),
    content text not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-- 2) Updated timestamp trigger
create or replace function public.set_updated_at()
returns trigger as $$
begin
  new.updated_at = now();
  return new;
end;
$$ language plpgsql;

drop trigger if exists trg_notes_set_updated_at on public.notes;
create trigger trg_notes_set_updated_at
before update on public.notes
for each row execute function public.set_updated_at();

-- 3) Row Level Security (RLS)
alter table public.notes enable row level security;

-- For demo/public app (no auth), allow all operations. If you later add auth,
-- replace these with per-user policies keyed by auth.uid().
drop policy if exists "Enable read for all users" on public.notes;
-- Using anon key only; scope at application layer with user_id.
create policy "Enable read" on public.notes for select using (true);

drop policy if exists "Enable insert for all users" on public.notes;
create policy "Enable insert" on public.notes for insert with check (true);

drop policy if exists "Enable update for all users" on public.notes;
create policy "Enable update" on public.notes for update using (true) with check (true);

drop policy if exists "Enable delete for all users" on public.notes;
create policy "Enable delete" on public.notes for delete using (true);

-- 4) Helpful index for search by title and time ordering
create index if not exists idx_notes_created_at on public.notes (created_at desc);
create index if not exists idx_notes_user_id on public.notes (user_id);
create index if not exists idx_notes_title_trgm on public.notes using gin (title gin_trgm_ops);


-- 5) Users table to store basic profile
create table if not exists public.users (
    id text primary key, -- firebase uid
    email text,
    display_name text,
    photo_url text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

drop trigger if exists trg_users_set_updated_at on public.users;
create trigger trg_users_set_updated_at
before update on public.users
for each row execute function public.set_updated_at();

alter table public.users enable row level security;
create policy if not exists "users read" on public.users for select using (true);
create policy if not exists "users upsert" on public.users for insert with check (true);
create policy if not exists "users update" on public.users for update using (true) with check (true);


