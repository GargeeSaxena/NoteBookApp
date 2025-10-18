# 🎯 START HERE - OneDrive Setup

Your project is on OneDrive. Follow these steps to build successfully!

---

## ⚡ DO THIS RIGHT NOW (5 minutes)

### Step 1: Exclude Build Folders from OneDrive

Open File Explorer and do this for each folder:

**Folder 1:**
```
Navigate to: C:\Users\HP\OneDrive\Desktop\NoteBook App\android\build
Right-click folder → "Free up space"
```

**Folder 2:**
```
Navigate to: C:\Users\HP\OneDrive\Desktop\NoteBook App\android\.gradle
Right-click folder → "Free up space"
```

**Folder 3:**
```
Navigate to: C:\Users\HP\OneDrive\Desktop\NoteBook App\android\app\build
Right-click folder → "Free up space"
```

✅ These folders will now stay on your PC and NOT sync to OneDrive!

---

### Step 2: Pause OneDrive

1. Look at your **system tray** (bottom-right corner of Windows)
2. Find the **OneDrive cloud icon** ☁️
3. **Right-click it**
4. Click **"Pause syncing"**
5. Select **"2 hours"**

✅ OneDrive won't interfere with your build now!

---

### Step 3: Prepare the Build

**Option A: Use the Helper Script**
1. Navigate to: `C:\Users\HP\OneDrive\Desktop\NoteBook App\android\`
2. **Double-click** `prepare_build.bat`
3. Follow the prompts

**Option B: Manual PowerShell**
```powershell
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"
.\gradlew --stop
timeout /t 3
Remove-Item -Recurse -Force .\app\build -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\build -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\.gradle -ErrorAction SilentlyContinue
```

---

### Step 4: Open Android Studio & Build

1. **Open Android Studio**
2. Open your project: `C:\Users\HP\OneDrive\Desktop\NoteBook App\android\`
3. Wait for indexing to complete
4. **File → Sync Project with Gradle Files**
5. **Build → Clean Project**
6. **Build → Rebuild Project**
7. **Run** ▶️ your app!

---

## 📱 If Build Succeeds - You're Done!

If the app builds and runs successfully:
- ✅ Your setup is working!
- Remember to **pause OneDrive** before each build session
- Read `WORKING_WITH_ONEDRIVE.md` for daily workflow tips

---

## ❌ If Build Still Fails

### Try This:
1. **Close Android Studio** completely
2. **Restart Windows**
3. **Before opening anything:**
   - Pause OneDrive (2 hours)
   - Run `prepare_build.bat`
4. Open Android Studio
5. Build → Clean Project
6. Build → Rebuild Project

### Still Having Issues?

Check these files for detailed help:
- `WORKING_WITH_ONEDRIVE.md` - Complete OneDrive guide
- `FIX_WINDOWS_FILE_LOCK.md` - File lock troubleshooting
- `TROUBLESHOOTING.md` - All build errors and solutions

---

## 🎯 Quick Reference

### Before Each Build Session:
```
1. Pause OneDrive (2 hours)
2. Open Android Studio
3. Build!
```

### After You're Done:
```
1. Close Android Studio
2. Wait 10 seconds
3. Resume OneDrive sync
```

### If You Get File Lock Error:
```
1. Close Android Studio
2. Run prepare_build.bat
3. Reopen Android Studio
4. Try again
```

---

## 💡 Pro Tip

**Use Command Line for Faster Builds:**

```powershell
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"
.\gradlew assembleDebug
```

Your APK will be at:
```
android\app\build\outputs\apk\debug\app-debug.apk
```

You can install this directly on your phone!

---

## ✅ Configuration Already Applied

I've already configured your project with:
- ✅ Gradle caching enabled
- ✅ Parallel builds enabled
- ✅ Better file handling settings
- ✅ Longer daemon timeout

All in `gradle.properties` - you don't need to do anything!

---

## 🚀 You're Ready!

**Next steps:**
1. Do Step 1 above (exclude folders) - **IMPORTANT!**
2. Pause OneDrive
3. Run `prepare_build.bat`
4. Open Android Studio
5. Build & run!

Good luck! 🎉

