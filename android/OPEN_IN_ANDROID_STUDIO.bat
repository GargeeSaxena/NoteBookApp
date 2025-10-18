@echo off
echo ============================================
echo Opening Android Project in Android Studio
echo ============================================
echo.

REM Get the current directory (android folder)
set PROJECT_DIR=%~dp0

REM Try to find Android Studio executable
set STUDIO_PATH=""

REM Check common installation paths
if exist "C:\Program Files\Android\Android Studio\bin\studio64.exe" (
    set STUDIO_PATH="C:\Program Files\Android\Android Studio\bin\studio64.exe"
)
if exist "%LOCALAPPDATA%\Programs\Android Studio\bin\studio64.exe" (
    set STUDIO_PATH="%LOCALAPPDATA%\Programs\Android Studio\bin\studio64.exe"
)
if exist "C:\Program Files (x86)\Android\Android Studio\bin\studio64.exe" (
    set STUDIO_PATH="C:\Program Files (x86)\Android\Android Studio\bin\studio64.exe"
)

if %STUDIO_PATH%=="" (
    echo ERROR: Android Studio not found in common locations.
    echo Please open Android Studio manually and select this folder:
    echo %PROJECT_DIR%
    echo.
    pause
    exit /b 1
)

echo Found Android Studio at: %STUDIO_PATH%
echo Opening project: %PROJECT_DIR%
echo.

REM Open Android Studio with this project
start "" %STUDIO_PATH% "%PROJECT_DIR%"

echo Android Studio should open shortly...
echo.
echo If you see any Gradle sync errors, make sure you've:
echo 1. Created local.properties from local.properties.example
echo 2. Added your Supabase credentials to local.properties
echo.
pause

