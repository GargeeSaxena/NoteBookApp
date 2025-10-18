# 🚀 Working with Android Studio on OneDrive

Your project is on OneDrive, which can cause file locking issues during builds. Here's how to make it work smoothly!

---

## ✅ QUICK SETUP (Do This Once)

### 1. Exclude Build Folders from OneDrive Sync

**These folders should NOT sync to OneDrive:**

1. Open File Explorer
2. Navigate to each folder below
3. Right-click → **"Always keep on this device"** → Then → **"Free up space"**

**Folders to exclude:**
```
C:\Users\HP\OneDrive\Desktop\NoteBook App\android\build
C:\Users\HP\OneDrive\Desktop\NoteBook App\android\.gradle
C:\Users\HP\OneDrive\Desktop\NoteBook App\android\app\build
C:\Users\HP\OneDrive\Desktop\NoteBook App\android\app\.cxx
```

### 2. Configure OneDrive Settings

1. Right-click **OneDrive icon** in system tray
2. **Settings → Sync and backup**
3. Click **"Advanced settings"**
4. Under **"Files On-Demand"** make sure it's enabled
5. Check **"Save space and download files as you use them"**

---

## 🔧 EVERY TIME YOU BUILD (Important!)

### Before Opening Android Studio:

**Option A: Pause OneDrive (Recommended)**
1. Right-click OneDrive icon in system tray
2. **Pause syncing → 2 hours**
3. Now open Android Studio and build

**Option B: Use the Helper Script**

I've created a batch file to make this easier. Just double-click:
```
android\fix_file_lock.bat
```

This will:
- Stop Gradle daemon
- Delete old build files
- Prepare for a clean build

---

## 📋 DAILY WORKFLOW

### Starting Your Day:
```
1. Pause OneDrive sync (2 hours)
2. Open Android Studio
3. File → Sync Project with Gradle Files
4. Build → Clean Project (first time only)
5. Start coding!
```

### When You're Done:
```
1. Close Android Studio
2. Wait 10 seconds
3. Resume OneDrive sync
4. Your code will sync to cloud
```

---

## 🐛 IF BUILD FAILS WITH FILE LOCK ERROR

### Quick Fix (Takes 30 seconds):

**In PowerShell:**
```powershell
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"
.\gradlew --stop
timeout /t 3
Remove-Item -Recurse -Force .\app\build -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\build -ErrorAction SilentlyContinue
```

**Then:**
- Reopen Android Studio
- Build → Clean Project
- Build → Rebuild Project

---

## ⚡ FASTER ALTERNATIVE: Use Gradle from Command Line

When OneDrive is causing issues, you can build from command line instead:

### Build APK:
```powershell
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"
.\gradlew assembleDebug
```

### Install on Device:
```powershell
.\gradlew installDebug
```

The APK will be at:
```
android\app\build\outputs\apk\debug\app-debug.apk
```

You can install this manually on your device or emulator!

---

## 🎯 BEST PRACTICES

### ✅ DO:
- Pause OneDrive before intensive builds
- Use `.\gradlew --stop` before closing Android Studio
- Keep build folders excluded from OneDrive
- Commit and push to Git regularly (OneDrive ≠ version control)

### ❌ DON'T:
- Build while OneDrive is actively syncing
- Keep multiple Android Studio windows open
- Force quit Android Studio (close properly)
- Rely on OneDrive as your only backup

---

## 🔄 GRADLE CONFIGURATION

I've added these settings to `gradle.properties` to help with OneDrive:

```properties
# Build cache helps avoid rebuilding unchanged files
org.gradle.caching=true

# Keeps Gradle daemon alive longer (less file lock issues)
org.gradle.daemon.idletimeout=7200000

# Parallel builds (faster)
org.gradle.parallel=true
```

---

## 📱 TESTING YOUR APP

### Option 1: Android Studio (Normal)
- Click Run ▶️ button
- Select device/emulator
- App installs and runs

### Option 2: Command Line (When OneDrive is problematic)
```powershell
# Connect your device via USB
# Enable USB debugging on device

# Build and install
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App\android"
.\gradlew installDebug

# Launch app manually on device
```

### Option 3: Build APK and Share
```powershell
.\gradlew assembleDebug
```
APK location: `android\app\build\outputs\apk\debug\app-debug.apk`

Send this APK to your phone and install!

---

## 💾 BACKUP STRATEGY

**OneDrive is good for syncing, but also use Git:**

### Quick Git Setup:
```bash
cd "C:\Users\HP\OneDrive\Desktop\NoteBook App"
git add .
git commit -m "Working Android client"
git push origin main
```

This way you have:
- ✅ OneDrive: Live syncing across devices
- ✅ Git/GitHub: Version control and history
- ✅ Double protection!

---

## 🆘 EMERGENCY: Nothing Works

If you keep getting file lock errors:

### Nuclear Option:
1. Close Android Studio
2. Restart Windows
3. **Before** opening Android Studio:
   - Pause OneDrive for 2 hours
4. Open Android Studio
5. Build → Clean Project
6. Build → Rebuild Project

### Last Resort:
Copy project to a non-OneDrive location temporarily:
```
To: C:\Temp\NoteBook App\android
```
Build there, copy APK back to OneDrive folder when done.

---

## ✅ CHECKLIST FOR SUCCESS

- [ ] Build folders excluded from OneDrive sync
- [ ] OneDrive paused before major builds
- [ ] Gradle daemon stopped before closing Android Studio
- [ ] Using latest Gradle properties (already configured)
- [ ] APK extraction location known
- [ ] Git repository set up for proper version control

---

## 🎉 YOU'RE ALL SET!

With these configurations, you should be able to work on your Android project on OneDrive without major issues!

**Key takeaway:** Always pause OneDrive sync before building. This solves 90% of problems.

---

**Questions?** Check:
- `TROUBLESHOOTING.md` - Build error solutions
- `FIX_WINDOWS_FILE_LOCK.md` - Detailed file lock fixes
- `fix_file_lock.bat` - Automated cleanup script

