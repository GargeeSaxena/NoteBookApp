# How to Find Your Supabase Credentials

This guide shows you exactly where to find the credentials needed for the Android app.

## What You Need

The Android app needs two values:
1. **Project URL** (e.g., `https://abcdefgh.supabase.co`)
2. **Anon/Public Key** (a long JWT token)

## Step-by-Step Instructions

### 1. Go to Supabase Dashboard

Visit: https://supabase.com/dashboard

Log in to your account.

### 2. Select Your Project

Click on the project you're using for the Notebook App.

### 3. Navigate to Project Settings

In the left sidebar, scroll down and click:
- **⚙️ Project Settings** (gear icon at the bottom)

### 4. Go to API Settings

In the Project Settings menu, click:
- **API** (should be the first option)

### 5. Find Your Credentials

On the API page, you'll see:

#### Project URL
```
Under "Project URL" section
Example: https://abcdefghijklmno.supabase.co
```
**Copy this entire URL including the https://**

#### Anon/Public Key
```
Under "Project API keys" section
Look for "anon" or "public" key
Example: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFiY2RlZmdoaWprbG1ubyIsInJvbGUiOiJhbm9uIiwiaWF0IjoxNjk4MzY0ODAwLCJleHAiOjIwMTM5NDA4MDB9.XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```
**Copy this entire token (it's very long, don't cut it off!)**

### 6. Add to local.properties

Open `android/local.properties` in a text editor and update:

```properties
supabase.url=https://abcdefghijklmno.supabase.co
supabase.anon.key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOi...
```

**Important:**
- No quotes around the values
- No spaces before or after the equals sign
- The anon key should be all on one line
- Make sure you don't accidentally add newlines

### 7. Save and Sync

1. Save the `local.properties` file
2. In Android Studio: **File** → **Sync Project with Gradle Files**
3. Wait for sync to complete
4. Run the app!

## Visual Reference

```
Supabase Dashboard
└── Your Project
    └── ⚙️ Project Settings (left sidebar, bottom)
        └── API (first option)
            ├── Configuration
            │   └── Project URL: [COPY THIS] 📋
            └── Project API keys
                └── anon public: [COPY THIS] 📋
```

## Example Configuration

Here's what your `local.properties` should look like (with fake values):

```properties
## This file must *NOT* be checked into Version Control Systems,
sdk.dir=C\:\\Users\\HP\\AppData\\Local\\Android\\Sdk

## Supabase Configuration
supabase.url=https://mkxytkpewzmaucgwhahs.supabase.co
supabase.anon.key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1reHl0a3Bld3ptYXVjZ3doYWhzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjk1MDI0MDAsImV4cCI6MjA0NTA3ODQwMH0.X1234567890abcdefghijklmnopqrstuvwxyz123456789
```

## Troubleshooting

### "Project URL is empty"
- Make sure you copied the full URL including `https://`
- Check there are no spaces around the equals sign
- Verify the line starts with `supabase.url=`

### "Anon key is empty"
- Make sure you copied the entire token (it's VERY long)
- Don't break it across multiple lines
- Check there are no spaces around the equals sign
- Verify the line starts with `supabase.anon.key=`

### "Build fails after adding credentials"
1. Clean project: **Build** → **Clean Project**
2. Sync Gradle: **File** → **Sync Project with Gradle Files**
3. Rebuild: **Build** → **Rebuild Project**

### "Still not working?"
- Make sure you're using the **anon** key, not the **service_role** key
- The anon key is safe to use in client apps
- The service_role key should NEVER be used in client apps

## Security Note

✅ **Safe to use:**
- Project URL
- Anon/Public key

These are safe to use in client applications. Supabase uses Row Level Security (RLS) to protect your data.

❌ **NEVER use:**
- Service Role key (in client apps)
- Database password

## Next Steps

After adding credentials:
1. ✅ Sync Gradle in Android Studio
2. ✅ Run the app
3. ✅ Create an account
4. ✅ Start using the app!

## Need More Help?

- Check `ANDROID_SETUP_GUIDE.md` for detailed troubleshooting
- Review `QUICKSTART.md` for a 5-minute setup guide
- Use `SETUP_CHECKLIST.md` to verify everything is configured

---

**Time to find credentials: ~2 minutes ⏱️**

