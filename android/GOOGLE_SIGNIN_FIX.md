# Google Sign-In Fix

## Current Issue

Google Sign-In is showing error: **"Unsupported provider: provider is not enabled"**

This means Google OAuth is not enabled in your Supabase project.

## Option 1: Enable Google OAuth in Supabase (Recommended for Production)

### Step 1: Get Google OAuth Credentials

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Go to **APIs & Services** → **Credentials**
4. Click **Create Credentials** → **OAuth 2.0 Client ID**
5. Choose **Web application**
6. Add Authorized redirect URIs:
   ```
   https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback
   ```
7. Copy your **Client ID** and **Client Secret**

### Step 2: Enable Google in Supabase

1. Go to your [Supabase Dashboard](https://supabase.com/dashboard)
2. Select your project
3. Go to **Authentication** → **Providers**
4. Find **Google** and click to enable it
5. Paste your **Client ID** and **Client Secret**
6. Click **Save**

### Step 3: Test Google Sign-In

Now the Google Sign-In button in the app should work!

---

## Option 2: Use Email/Password Authentication (Works Now!)

**The app already works with email/password!** You don't need to set up Google OAuth to use the app.

### Just use Email/Password:

1. Open the app
2. Enter any email (e.g., `test@example.com`)
3. Enter a password (minimum 6 characters, e.g., `test123`)
4. Click **Sign Up** to create account
5. Or click **Sign In** if account already exists

**This works immediately - no setup required!**

---

## Recommendation

**For Testing**: Use email/password (Option 2) - it's already working!  
**For Production**: Set up Google OAuth (Option 1) for better user experience

---

## After Enabling Google OAuth

If you enable Google OAuth in Supabase, the Google Sign-In button in the Android app will automatically work with no code changes needed!

