-- Minimal migration to add user_id to notes if missing
alter table if exists public.notes
  add column if not exists user_id text not null default '';

-- Optional: index for faster lookups by user
create index if not exists idx_notes_user_id on public.notes (user_id);

-- Optional: drop the default after backfilling (safe to run later)
-- alter table public.notes alter column user_id drop default;


