@echo off
echo ========================================
echo REBUILDING NOTEBOOK APP - PLEASE WAIT
echo ========================================
echo.

cd /d "%~dp0"

echo Step 1: Stopping Gradle daemon...
call gradlew --stop
timeout /t 2 /nobreak >nul

echo Step 2: Cleaning project...
call gradlew clean
if errorlevel 1 (
    echo ERROR: Clean failed!
    pause
    exit /b 1
)

echo Step 3: Building project...
call gradlew build
if errorlevel 1 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo BUILD COMPLETE!
echo ========================================
echo.
echo NEXT STEPS:
echo 1. On your phone: Uninstall the NoteBook app completely
echo 2. In Android Studio: Click the green Run button
echo 3. Wait for the new app to install
echo.
echo Press any key to exit...
pause >nul

