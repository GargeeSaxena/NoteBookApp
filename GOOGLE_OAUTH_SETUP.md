# 🔐 Google OAuth Setup - Detailed Guide

Follow these steps EXACTLY as written. Take your time!

---

## PART 1: Create OAuth Credentials in Google Cloud

### Step 1: Open Google Cloud Console

1. Open your browser
2. Go to: **https://console.cloud.google.com**
3. Sign in with your Google account

### Step 2: Create or Select a Project

**If you have an existing project:**
- Click the project dropdown at the top (next to "Google Cloud")
- Select your project

**If you need a new project:**
- Click the project dropdown at the top
- Click "NEW PROJECT"
- Project name: "My Notes App" (or whatever you like)
- Click "CREATE"
- Wait a few seconds for it to create
- Select the new project from the dropdown

### Step 3: Enable Google+ API (Required!)

1. In the left sidebar, click "APIs & Services" → "Enabled APIs & services"
2. Click "+ ENABLE APIS AND SERVICES" at the top
3. Search for "Google+ API"
4. Click on it
5. Click "ENABLE"
6. Wait for it to enable (a few seconds)

### Step 4: Create OAuth Consent Screen

1. In the left sidebar, click "OAuth consent screen"
2. Select "External" (unless you have a Google Workspace)
3. Click "CREATE"

**Fill out the form:**
- App name: `My Notes App`
- User support email: (your email - should be pre-filled)
- App logo: (skip this)
- Application home page: (leave empty for now)
- Authorized domains: (skip for now)
- Developer contact email: (your email)
- Click "SAVE AND CONTINUE"

**Scopes:**
- Click "SAVE AND CONTINUE" (we don't need to add scopes manually)

**Test users:**
- Click "+ ADD USERS"
- Add your email address
- Click "ADD"
- Click "SAVE AND CONTINUE"

**Summary:**
- Review and click "BACK TO DASHBOARD"

### Step 5: Create OAuth Client ID

1. In the left sidebar, click "Credentials"
2. Click "+ CREATE CREDENTIALS" at the top
3. Select "OAuth client ID"

**Configure the OAuth client:**
- Application type: Select "Web application"
- Name: `My Notes Web App`

**Authorized JavaScript origins:**
- Click "+ ADD URI"
- Add: `https://mkxytkpewzmaucgwhahs.supabase.co`
- Click "+ ADD URI" again
- Add: `http://localhost:3000` (for local testing)

**Authorized redirect URIs:**
- Click "+ ADD URI"
- Add this EXACTLY (copy-paste it):
  ```
  https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback
  ```
- For local testing, click "+ ADD URI" again and add:
  ```
  http://localhost:3000
  ```

- Click "CREATE"

### Step 6: Copy Your Credentials

A popup will appear with your credentials:
- **Client ID**: Something like `123456789-abcdefghijklmnop.apps.googleusercontent.com`
- **Client Secret**: Something like `GOCSPX-aBcDeFgHiJkLmNoPqRsTuVwXyZ`

**IMPORTANT:** 
- Keep this window open OR
- Copy both values to a notepad/text file
- You'll need them in the next part!

---

## PART 2: Configure Supabase

### Step 1: Open Supabase Dashboard

1. Open a new browser tab
2. Go to: **https://app.supabase.com**
3. Sign in to your account

### Step 2: Select Your Project

1. You should see your projects
2. Click on: **note-app-c70ad** (your project)

### Step 3: Navigate to Authentication Settings

1. In the left sidebar, click "Authentication" (shield icon)
2. Click "Providers" tab at the top
3. Scroll down to find "Google"
4. Click on "Google" to expand it

### Step 4: Enable and Configure Google Provider

1. Toggle the switch to **ON** (top right of the Google section)
2. You'll see these fields:
   - **Client ID (for OAuth)**
   - **Client Secret (for OAuth)**

3. Go back to your Google Cloud Console window (from Part 1, Step 6)
4. Copy the **Client ID**
5. Paste it into Supabase's "Client ID" field
6. Copy the **Client Secret** from Google Cloud Console
7. Paste it into Supabase's "Client Secret" field

8. Leave other settings as default

9. Click "Save" at the bottom

### Step 5: Verify Configuration

You should see:
- Google provider is **enabled** (toggle is on)
- Client ID is filled in
- Client Secret is filled in (shows as dots/asterisks)
- Status shows as configured

---

## ✅ DONE! Now Test It

### Test Locally:

1. Open terminal in your project folder
2. Run: `npm start`
3. Open: `http://localhost:3000`
4. Click "Sign in / Sign up with Google"
5. You should be redirected to Google
6. Sign in with your Google account
7. You should be redirected back and signed in!

### If It Works Locally:

**Deploy to Render:**

1. Go to Render dashboard
2. Find your service
3. Go to "Environment" tab
4. Add these variables (if not already there):
   ```
   SUPABASE_URL=https://mkxytkpewzmaucgwhahs.supabase.co
   SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1reHl0a3Bld3ptYXVjZ3doYWhzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjAzNjk0MjIsImV4cCI6MjA3NTk0NTQyMn0.rc0yztwHPANCQyoKiEa6PC8l8ufoGnzZh4KonqAaOMo
   ```
4. Save
5. Commit and push your code to GitHub
6. Render will auto-deploy
7. Test on your live URL!

---

## 🐛 Troubleshooting

### "Error: redirect_uri_mismatch"
- Go back to Google Cloud Console → Credentials
- Edit your OAuth Client
- Make sure the redirect URI is EXACTLY: `https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`
- Save and try again

### Sign-in button does nothing
- Check browser console (F12) for errors
- Make sure Google provider is enabled in Supabase
- Verify Client ID and Secret are saved in Supabase

### "Access blocked: This app's request is invalid"
- Make sure you added your email as a test user in Google Cloud Console
- Go to OAuth consent screen → Test users → Add your email

### Still not working?
- Double-check all steps above
- Make sure environment variables are set correctly
- Check that your app is running (`npm start`)
- Clear browser cache and try again

---

## 📸 What You Should See

### In Google Cloud Console:
- OAuth client created
- Redirect URI: `https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`
- Client ID and Secret generated

### In Supabase:
- Google provider toggle: ON
- Client ID: filled
- Client Secret: filled (shows as dots)
- Save button clicked

### In Your App:
- Sign-in button visible
- Clicking redirects to Google
- After signing in, redirects back
- Notes interface appears
- You can create notes!

---

## 🎉 Success!

If sign-in works, you're done! Your authentication is fully configured and working.

---

## 📞 Need More Help?

If something isn't clear:
1. Take a screenshot of what you see
2. Check browser console (F12) for errors
3. Verify each step above carefully
4. Make sure you're using the EXACT URLs provided

The most common mistake is a typo in the redirect URI - it must be EXACTLY:
`https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`

Good luck! 🚀

