@echo off
echo ========================================
echo  Prepare Android Build (OneDrive Safe)
echo ========================================
echo.
echo This will prepare your project for building.
echo.
echo What this does:
echo 1. Stop any running Gradle daemon
echo 2. Wait a moment for files to unlock
echo 3. Clean build folders safely
echo.
echo REMINDER: Pause OneDrive sync first!
echo (Right-click OneDrive icon ^> Pause syncing ^> 2 hours)
echo.
pause

echo.
echo [1/3] Stopping Gradle daemon...
call gradlew --stop
echo     Done!

echo.
echo [2/3] Waiting for file locks to clear...
timeout /t 5 /nobreak >nul
echo     Done!

echo.
echo [3/3] Cleaning build folders...

if exist "app\build" (
    echo     Cleaning app\build...
    rmdir /s /q "app\build" 2>nul
    if exist "app\build" (
        echo     WARNING: Some files in app\build are still locked
        echo     Try closing Android Studio completely
    ) else (
        echo     app\build cleaned successfully
    )
) else (
    echo     app\build already clean
)

if exist "build" (
    echo     Cleaning build...
    rmdir /s /q "build" 2>nul
    if exist "build" (
        echo     WARNING: Some files in build are still locked
    ) else (
        echo     build cleaned successfully
    )
) else (
    echo     build already clean
)

if exist ".gradle" (
    echo     Cleaning .gradle cache...
    rmdir /s /q ".gradle" 2>nul
    if exist ".gradle" (
        echo     WARNING: Some files in .gradle are still locked
    ) else (
        echo     .gradle cleaned successfully
    )
) else (
    echo     .gradle already clean
)

echo.
echo ========================================
echo  READY TO BUILD!
echo ========================================
echo.
echo Next steps:
echo 1. Open Android Studio (if not already open)
echo 2. File ^> Sync Project with Gradle Files
echo 3. Build ^> Clean Project
echo 4. Build ^> Rebuild Project
echo 5. Run your app!
echo.
echo If you see file lock errors:
echo - Close Android Studio
echo - Run this script again
echo - Make sure OneDrive is paused
echo.
pause

