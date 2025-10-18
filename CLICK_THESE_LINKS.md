# 🔗 Click These Links - OAuth Setup

## 📌 Step-by-Step with Direct Links

### Part 1: Google Cloud Console

**1. Create OAuth Credentials:**
- Click here: [Google Cloud Credentials](https://console.cloud.google.com/apis/credentials)
- Sign in if needed
- Click the blue "+ CREATE CREDENTIALS" button
- Select "OAuth client ID"

**2. Enable Google+ API (if needed):**
- Click here: [Enable Google+ API](https://console.cloud.google.com/apis/library/plus.googleapis.com)
- Click "ENABLE"

**3. OAuth Consent Screen (if needed):**
- Click here: [OAuth Consent Screen](https://console.cloud.google.com/apis/credentials/consent)
- Configure as "External"
- Fill in required fields
- Add your email as test user

**4. When creating OAuth Client:**
```
Application Type: Web application
Name: My Notes Web App

Authorized redirect URIs - Add this:
https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback
```

**5. Copy the Client ID and Client Secret that appear!**

---

### Part 2: Supabase Dashboard

**1. Go to your project:**
- Click here: [Supabase Projects](https://app.supabase.com/projects)
- Select: **note-app-c70ad**

**2. Authentication Providers:**
- Click here: [Auth Providers](https://app.supabase.com/project/_/auth/providers)
  (Replace _ with your project ref if link doesn't work)
- Or manually: Left sidebar → Authentication → Providers tab

**3. Configure Google:**
- Scroll to "Google"
- Toggle it ON
- Paste your Client ID (from Google)
- Paste your Client Secret (from Google)
- Click "Save"

---

## ✅ Quick Test

**Test Locally:**
```bash
npm start
```
Then open: http://localhost:3000

**Test Live:**
After deploying, open your Render URL and try signing in!

---

## 🎯 Critical Information

**Your Supabase Project:**
- Project: note-app-c70ad
- URL: https://mkxytkpewzmaucgwhahs.supabase.co

**Required Redirect URI (copy exactly):**
```
https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback
```

**Environment Variables for Render:**
```
SUPABASE_URL=https://mkxytkpewzmaucgwhahs.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1reHl0a3Bld3ptYXVjZ3doYWhzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjAzNjk0MjIsImV4cCI6MjA3NTk0NTQyMn0.rc0yztwHPANCQyoKiEa6PC8l8ufoGnzZh4KonqAaOMo
```

---

## 🆘 Quick Troubleshooting

**"redirect_uri_mismatch" error?**
→ Check that redirect URI in Google Cloud is exactly: 
`https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`

**Sign-in button does nothing?**
→ Open browser console (F12) and check for errors
→ Verify Google provider is enabled in Supabase

**Still not working?**
→ Open `GOOGLE_OAUTH_SETUP.md` for detailed instructions

---

## 🎉 That's It!

Once you complete these steps, your authentication will work perfectly!

