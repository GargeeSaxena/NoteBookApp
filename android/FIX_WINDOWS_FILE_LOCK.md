# 🔴 WINDOWS FILE LOCK ERROR - QUICK FIX

## The Problem
```
java.nio.file.AccessDeniedException: 
C:\Users\HP\OneDrive\Desktop\NoteBook App\android\app\build\intermediates\...
```

**What's happening:** Gradle can't delete build files because:
- Windows/OneDrive is locking files
- Gradle daemon has files open
- Android Studio hasn't released file handles

---

## ✅ SOLUTION (Takes 2 minutes)

### Step 1: Close Android Studio COMPLETELY
1. **Close all Android Studio windows**
2. **Wait 5 seconds** for it to fully exit
3. Check Task Manager - make sure no `studio64.exe` process is running

### Step 2: Stop Gradle Daemon
Open **PowerShell** or **Command Prompt** and run:

```powershell
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"
.\gradlew --stop
```

Wait for: `"Stopping Daemon(s)"`

### Step 3: Delete Build Folders
Still in PowerShell, run:

```powershell
# Delete app build folder
Remove-Item -Recurse -Force .\app\build -ErrorAction SilentlyContinue

# Delete root build folder
Remove-Item -Recurse -Force .\build -ErrorAction SilentlyContinue

# Delete Gradle cache
Remove-Item -Recurse -Force .\.gradle -ErrorAction SilentlyContinue
```

**If that fails (file locked):**
```powershell
# Pause OneDrive temporarily
# Right-click OneDrive icon in system tray → Pause syncing → 2 hours

# Then try deleting again
Remove-Item -Recurse -Force .\app\build
Remove-Item -Recurse -Force .\build
Remove-Item -Recurse -Force .\.gradle
```

### Step 4: Reopen & Rebuild
1. **Reopen Android Studio**
2. Wait for indexing to finish
3. **File → Invalidate Caches / Restart**
4. After restart:
   - **File → Sync Project with Gradle Files**
   - **Build → Clean Project**
   - **Build → Rebuild Project**
5. **Run** ▶️

---

## 🚨 If Still Locked

### Option A: Restart Computer
The nuclear option that always works:
1. **Restart Windows**
2. Open Android Studio
3. Clean & Rebuild

### Option B: Use Command Line Build
```powershell
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"
.\gradlew clean
.\gradlew assembleDebug
```

### Option C: Move Project Out of OneDrive
OneDrive can cause file locking issues:

1. **Copy** the entire project to: `C:\AndroidProjects\NoteBook App\`
2. Open that copy in Android Studio
3. Build there

---

## 💡 PREVENT THIS IN THE FUTURE

### 1. Exclude from OneDrive Sync
Right-click the `android` folder → **Free up space**
Or add to OneDrive exclusions.

### 2. Exclude from Windows Defender
1. Windows Security → Virus & threat protection → Settings
2. **Exclusions → Add folder**
3. Add: `C:\Users\HP\OneDrive\Desktop\NoteBook App\android\build`
4. Add: `C:\Users\HP\OneDrive\Desktop\NoteBook App\android\app\build`
5. Add: `C:\Users\HP\OneDrive\Desktop\NoteBook App\android\.gradle`

### 3. Use Local Project Location
Instead of OneDrive, keep projects in:
- `C:\AndroidProjects\`
- `C:\Users\HP\Documents\AndroidProjects\`

---

## 📋 QUICK CHECKLIST

- [ ] Close Android Studio completely
- [ ] Stop Gradle daemon: `.\gradlew --stop`
- [ ] Delete `app\build`, `build`, `.gradle` folders
- [ ] (If needed) Pause OneDrive sync
- [ ] Reopen Android Studio
- [ ] Invalidate Caches & Restart
- [ ] Clean Project
- [ ] Rebuild Project
- [ ] Run app

---

## 🎯 TL;DR - Copy & Paste These Commands

```powershell
# Step 1: Navigate to android folder
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"

# Step 2: Stop Gradle
.\gradlew --stop

# Step 3: Clean build folders
Remove-Item -Recurse -Force .\app\build -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\build -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\.gradle -ErrorAction SilentlyContinue

# Step 4: Reopen Android Studio and rebuild
```

**If commands fail:** Close Android Studio first, then try again.

**If still fails:** Restart computer, then try.

---

## ✅ Success Indicator

You'll know it worked when:
- Build folders are deleted
- Android Studio opens without errors
- Gradle sync completes successfully
- Build starts without `AccessDeniedException`

---

**This is a Windows + OneDrive issue, not a code issue.** Your app code is fine! 🎉


