# 👀 What You Will See - Visual Guide

This guide shows you what to expect at each step so you know you're on the right track!

---

## 🟦 GOOGLE CLOUD CONSOLE

### Screen 1: Credentials Page
**URL:** https://console.cloud.google.com/apis/credentials

**What you'll see:**
```
┌─────────────────────────────────────────┐
│ APIs & Services > Credentials          │
├─────────────────────────────────────────┤
│                                         │
│  + CREATE CREDENTIALS  ▼               │
│                                         │
│  OAuth 2.0 Client IDs                  │
│  API Keys                               │
│  Service Accounts                       │
│                                         │
└─────────────────────────────────────────┘
```

**What to do:** Click "+ CREATE CREDENTIALS" → Select "OAuth client ID"

---

### Screen 2: Create OAuth Client ID
**What you'll see:**
```
┌─────────────────────────────────────────┐
│ Create OAuth client ID                  │
├─────────────────────────────────────────┤
│                                         │
│ Application type:                       │
│   ○ Web application                     │
│   ○ Android                             │
│   ○ iOS                                 │
│                                         │
│ Name: [________________]                │
│                                         │
│ Authorized JavaScript origins:          │
│   [+ ADD URI]                           │
│                                         │
│ Authorized redirect URIs:               │
│   [+ ADD URI]                           │
│                                         │
│         [CANCEL]  [CREATE]              │
└─────────────────────────────────────────┘
```

**What to do:**
1. Select "Web application"
2. Name: "My Notes Web App"
3. Click "+ ADD URI" under redirect URIs
4. Add: `https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`
5. Click "CREATE"

---

### Screen 3: OAuth Client Created (IMPORTANT!)
**What you'll see:**
```
┌─────────────────────────────────────────┐
│ OAuth client created                    │
├─────────────────────────────────────────┤
│                                         │
│ Here's your client ID and secret:      │
│                                         │
│ Client ID:                              │
│ 123456-abc.apps.googleusercontent.com   │
│                                         │
│ Client secret:                          │
│ GOCSPX-aBcDeFgHiJkLmNoPqRsTuV          │
│                                         │
│         [DOWNLOAD JSON]  [OK]           │
└─────────────────────────────────────────┘
```

**CRITICAL:** Copy both the Client ID and Client Secret!
- Keep this window open OR
- Copy them to a text file
- You'll need them for Supabase!

---

## 🟩 SUPABASE DASHBOARD

### Screen 1: Project Selection
**URL:** https://app.supabase.com

**What you'll see:**
```
┌─────────────────────────────────────────┐
│ All projects                            │
├─────────────────────────────────────────┤
│                                         │
│  📦 note-app-c70ad                      │
│     Active                              │
│     [SELECT]                            │
│                                         │
└─────────────────────────────────────────┘
```

**What to do:** Click on "note-app-c70ad"

---

### Screen 2: Authentication Page
**What you'll see (left sidebar):**
```
┌──────────────────┐
│ 🏠 Home          │
│ 📊 Table Editor  │
│ 🔐 Authentication│ ← Click here
│ 📁 Storage       │
│ ⚡ Edge Functions│
└──────────────────┘
```

**What to do:** Click "Authentication" in sidebar

---

### Screen 3: Providers Tab
**What you'll see (top tabs):**
```
┌─────────────────────────────────────────┐
│ Users | Providers | Policies | ...      │
│       ▔▔▔▔▔▔▔▔▔                         │
└─────────────────────────────────────────┘
```

**What to do:** Click "Providers" tab

---

### Screen 4: Google Provider Configuration
**What you'll see:**
```
┌─────────────────────────────────────────┐
│ Auth Providers                          │
├─────────────────────────────────────────┤
│                                         │
│ Email                         [ON]      │
│                                         │
│ Phone                         [OFF]     │
│                                         │
│ ▼ Google                      [OFF]     │  ← Click here
│                                         │
└─────────────────────────────────────────┘
```

**What to do:** Click on "Google" to expand it

---

### Screen 5: Google Provider Settings (Expanded)
**What you'll see:**
```
┌─────────────────────────────────────────┐
│ ▼ Google                      [ ON ]    │ ← Toggle this ON
├─────────────────────────────────────────┤
│                                         │
│ Enable Google provider                  │
│                                         │
│ Client ID (for OAuth) *                 │
│ [________________________________]      │
│                                         │
│ Client Secret (for OAuth) *             │
│ [________________________________]      │
│                                         │
│ Skip nonce check                        │
│ [ ] Skip nonce...                       │
│                                         │
│              [SAVE]                     │
└─────────────────────────────────────────┘
```

**What to do:**
1. Toggle switch to ON
2. Paste Client ID (from Google Cloud Console)
3. Paste Client Secret (from Google Cloud Console)
4. Click "SAVE"

---

### Screen 6: Success!
**What you'll see:**
```
┌─────────────────────────────────────────┐
│ ✓ Successfully updated settings         │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│ ▼ Google                      [ ON ]    │ ← Should be green/on
├─────────────────────────────────────────┤
│ Client ID: 123456...googleusercontent   │
│ Client Secret: ●●●●●●●●●●●●●●●●●●●●    │
│                                         │
│ Status: ✓ Configured                    │
└─────────────────────────────────────────┘
```

**You're done with configuration!** ✅

---

## 🧪 TESTING

### Screen 1: Your App (Not Signed In)
**URL:** http://localhost:3000

**What you'll see:**
```
┌─────────────────────────────────────────┐
│           📝 My Notes                   │
├─────────────────────────────────────────┤
│                                         │
│     Please sign in to continue          │
│                                         │
│  [Sign in / Sign up with Google]        │
│                                         │
└─────────────────────────────────────────┘
```

**What to do:** Click the blue button

---

### Screen 2: Google Sign-In
**What you'll see:**

Google's sign-in page will appear:
```
┌─────────────────────────────────────────┐
│            🔵 Google                    │
├─────────────────────────────────────────┤
│                                         │
│  Choose an account                      │
│                                         │
│  📧 your.email@gmail.com                │
│                                         │
│  Use another account                    │
│                                         │
└─────────────────────────────────────────┘
```

**What to do:** Select your Google account

---

### Screen 3: Permission Request
**What you'll see:**
```
┌─────────────────────────────────────────┐
│  My Notes App wants to access your      │
│  Google Account                         │
│                                         │
│  This will allow My Notes App to:       │
│  • See your personal info               │
│  • See your email address               │
│                                         │
│         [Cancel]  [Allow]               │
└─────────────────────────────────────────┘
```

**What to do:** Click "Allow"

---

### Screen 4: Success! (Signed In)
**What you'll see:**
```
┌─────────────────────────────────────────┐
│  📝 My Notes — Your Name   [Sign out]   │
├─────────────────────────────────────────┤
│                                         │
│  Create New Note                        │
│  ┌─────────────────────────────────┐   │
│  │ Note title...                   │   │
│  ├─────────────────────────────────┤   │
│  │ Write your note here...         │   │
│  │                                 │   │
│  └─────────────────────────────────┘   │
│             [Add Note]                  │
│                                         │
│  Your Notes                             │
│  (No notes yet. Create your first!)     │
│                                         │
└─────────────────────────────────────────┘
```

**You're signed in!** 🎉

Now you can:
- Create notes
- View your notes
- Sign out
- Everything works!

---

## ✅ CHECKLIST

After completing setup, verify:

- ✅ Google Cloud Console shows OAuth client created
- ✅ Redirect URI is exactly: `https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`
- ✅ Supabase shows Google provider enabled (toggle ON)
- ✅ Client ID and Secret are saved in Supabase
- ✅ Clicking sign-in redirects to Google
- ✅ After signing in, you're back at the app
- ✅ You can see the notes interface
- ✅ You can create notes

---

## 🐛 Common Issues

### Issue: "redirect_uri_mismatch"
**You'll see:**
```
Error 400: redirect_uri_mismatch
The redirect URI in the request does not match...
```

**Fix:** Go back to Google Cloud Console → Credentials → Edit OAuth Client
Make sure redirect URI is EXACTLY: 
`https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`

### Issue: "Access blocked: This app's request is invalid"
**Fix:** 
1. Go to Google Cloud Console → OAuth consent screen
2. Click "ADD USERS" under Test users
3. Add your email address
4. Save and try again

### Issue: Nothing happens when clicking sign-in button
**Fix:**
1. Open browser console (Press F12)
2. Look for error messages
3. Make sure environment variables are set
4. Check that Google provider is enabled in Supabase

---

## 🎓 Summary

You should see these screens in order:
1. Google Cloud Console → Create OAuth client
2. Copy Client ID and Secret
3. Supabase → Enable Google provider
4. Paste credentials
5. Test → Google sign-in page appears
6. Authorize app
7. Redirected back → Signed in!

If you see all these screens, everything is working! 🎉

---

Need more help? Check:
- **GOOGLE_OAUTH_SETUP.md** for detailed instructions
- **SIMPLE_OAUTH_STEPS.txt** for a checklist
- **CLICK_THESE_LINKS.md** for direct URLs

