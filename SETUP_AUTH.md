# Authentication Setup Guide

Your authentication has been rebuilt using **Supabase Auth** for simplicity and reliability.

## ✅ What Was Changed

1. **Removed Firebase** - Eliminated the complex Firebase redirect flow
2. **Added Supabase Auth** - Simple, modern authentication with Google OAuth
3. **Simpler Code** - Much cleaner and easier to maintain

## 🔧 Setup Instructions

### Step 1: Configure Google OAuth in Supabase

1. Go to your Supabase Dashboard: https://app.supabase.com
2. Select your project: **note-app-c70ad**
3. Click on **Authentication** in the left sidebar
4. Click on **Providers**
5. Find **Google** and click to configure it
6. Enable Google provider
7. You'll need to create OAuth credentials in Google Cloud Console:
   - Go to: https://console.cloud.google.com/apis/credentials
   - Create a new OAuth 2.0 Client ID
   - Set **Authorized redirect URIs** to: `https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`
   - Copy the Client ID and Client Secret
8. Paste the Client ID and Client Secret into Supabase
9. Save the configuration

### Step 2: Set Environment Variables on Render

Go to your Render dashboard and set these environment variables:

```
SUPABASE_URL=https://mkxytkpewzmaucgwhahs.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1reHl0a3Bld3ptYXVjZ3doYWhzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjAzNjk0MjIsImV4cCI6MjA3NTk0NTQyMn0.rc0yztwHPANCQyoKiEa6PC8l8ufoGnzZh4KonqAaOMo
PORT=3000
```

### Step 3: Install New Dependencies

Run this command in your project directory:

```bash
npm install
```

This will install:
- `dotenv` - For loading environment variables locally
- Updated Supabase client (already in package.json)

### Step 4: Test Locally (Optional)

If you want to test locally, create a `.env` file with the same environment variables:

```env
SUPABASE_URL=https://mkxytkpewzmaucgwhahs.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1reHl0a3Bld3ptYXVjZ3doYWhzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjAzNjk0MjIsImV4cCI6MjA3NTk0NTQyMn0.rc0yztwHPANCQyoKiEa6PC8l8ufoGnzZh4KonqAaOMo
PORT=3000
```

Then run:

```bash
npm start
```

### Step 5: Deploy

Commit your changes and push to GitHub. Render will automatically redeploy with the new code.

## 🎉 How It Works Now

1. User clicks "Sign in / Sign up with Google"
2. Supabase handles the OAuth flow
3. User authorizes with Google
4. User is redirected back to your app, automatically signed in
5. Notes are fetched and displayed

## 🔒 Security Notes

- The Supabase anon key is safe to expose publicly (it's designed for client-side use)
- Row Level Security (RLS) policies protect your data
- User authentication is handled entirely by Supabase
- No complex redirect flows to debug

## 🐛 Troubleshooting

**If sign-in doesn't work:**
1. Check that Google OAuth is properly configured in Supabase
2. Verify the redirect URI is correct in Google Cloud Console
3. Make sure environment variables are set correctly on Render
4. Check browser console for error messages

**If notes don't load:**
1. Verify database tables exist (run schema.sql in Supabase SQL Editor)
2. Check that RLS policies are enabled
3. Verify environment variables are correct

## 📝 Note

You no longer need the Firebase credentials. The app now uses only Supabase for both authentication and database.

