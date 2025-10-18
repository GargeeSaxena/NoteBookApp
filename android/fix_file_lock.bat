@echo off
echo ========================================
echo  Android Build File Lock Fix
echo ========================================
echo.
echo This will:
echo 1. Stop Gradle daemon
echo 2. Delete build folders
echo 3. Clean Gradle cache
echo.
echo IMPORTANT: Close Android Studio first!
echo.
pause

echo.
echo Stopping Gradle daemon...
call gradlew --stop
echo.

echo Deleting build folders...
if exist "app\build" (
    echo Deleting app\build...
    rmdir /s /q "app\build" 2>nul
)

if exist "build" (
    echo Deleting build...
    rmdir /s /q "build" 2>nul
)

if exist ".gradle" (
    echo Deleting .gradle...
    rmdir /s /q ".gradle" 2>nul
)

echo.
echo ========================================
echo  DONE!
echo ========================================
echo.
echo Next steps:
echo 1. Open Android Studio
echo 2. File ^> Invalidate Caches / Restart
echo 3. File ^> Sync Project with Gradle Files
echo 4. Build ^> Clean Project
echo 5. Build ^> Rebuild Project
echo 6. Run the app
echo.
echo If files are still locked:
echo - Pause OneDrive syncing
echo - Run this script again
echo - Or restart your computer
echo.
pause


