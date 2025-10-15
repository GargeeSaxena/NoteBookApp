@echo off
echo Initializing Git repository...
git init

echo Adding all files to Git...
git add .

echo Creating initial commit...
git commit -m "Initial commit: Add Notes App with create and read functionality"

echo Setting up remote origin...
git remote add origin https://github.com/GargeeSaxena/NoteBookApp.git

echo Pushing to GitHub...
git branch -M main
git push -u origin main

echo Done! Your code has been pushed to GitHub.
pause
