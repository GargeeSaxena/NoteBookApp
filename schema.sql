-- Supabase SQL schema for NoteBook App
-- Run this in Supabase SQL editor.

-- 1) Notes table
create table if not exists public.notes (
    id uuid primary key default gen_random_uuid(),
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
create policy "Enable read for all users"
  on public.notes for select
  using (true);

drop policy if exists "Enable insert for all users" on public.notes;
create policy "Enable insert for all users"
  on public.notes for insert
  with check (true);

drop policy if exists "Enable update for all users" on public.notes;
create policy "Enable update for all users"
  on public.notes for update
  using (true)
  with check (true);

drop policy if exists "Enable delete for all users" on public.notes;
create policy "Enable delete for all users"
  on public.notes for delete
  using (true);

-- 4) Helpful index for search by title and time ordering
create index if not exists idx_notes_created_at on public.notes (created_at desc);
create index if not exists idx_notes_title_trgm on public.notes using gin (title gin_trgm_ops);

-- Extensions (enable if not already enabled in your project)
create extension if not exists pg_trgm with schema public;
create extension if not exists pgcrypto with schema public; -- for gen_random_uuid


