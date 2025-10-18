@echo off
echo ========================================
echo  Exclude Build Folders from OneDrive
echo ========================================
echo.
echo This will mark build folders as SYSTEM files.
echo OneDrive will ignore them and not try to sync.
echo.
echo You only need to run this ONCE.
echo.
pause

cd /d "%~dp0"

echo.
echo Creating build folders if they don't exist...
if not exist "app\build" mkdir "app\build"
if not exist "build" mkdir "build"
if not exist ".gradle" mkdir ".gradle"

echo.
echo Marking folders as SYSTEM (OneDrive will ignore)...
attrib +S "app\build" /S /D
attrib +S "build" /S /D
attrib +S ".gradle" /S /D

echo.
echo ========================================
echo  SUCCESS!
echo ========================================
echo.
echo Build folders are now marked as system files.
echo OneDrive will NOT sync them anymore.
echo.
echo Next steps:
echo 1. Open Android Studio
echo 2. Build your project
echo 3. No more file locks!
echo.
pause

