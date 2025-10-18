# App Icon Setup

The app currently uses Android's **default app icon** to avoid build errors.

## ✅ QUICK FIX: Generate Custom Icons (Recommended)

### Using Android Studio's Built-in Tool:

1. Right-click on `app/src/main/res` folder
2. Select **New → Image Asset**
3. Choose **Launcher Icons (Adaptive and Legacy)**
4. Options:
   - **Foreground Layer**: Upload your icon image (512x512 PNG recommended)
   - **Background Layer**: Choose a color or image
5. Adjust padding and shape in preview
6. Click **Next** then **Finish**

Android Studio will automatically generate all required icon sizes and formats for all screen densities.

## Option 2: Manual Setup

If you have pre-made icons, place them in these folders:

```
app/src/main/res/
├── mipmap-mdpi/
│   ├── ic_launcher.png (48x48)
│   └── ic_launcher_round.png (48x48)
├── mipmap-hdpi/
│   ├── ic_launcher.png (72x72)
│   └── ic_launcher_round.png (72x72)
├── mipmap-xhdpi/
│   ├── ic_launcher.png (96x96)
│   └── ic_launcher_round.png (96x96)
├── mipmap-xxhdpi/
│   ├── ic_launcher.png (144x144)
│   └── ic_launcher_round.png (144x144)
└── mipmap-xxxhdpi/
    ├── ic_launcher.png (192x192)
    └── ic_launcher_round.png (192x192)
```

## Option 3: Online Generator

Use online tools like:
- https://romannurik.github.io/AndroidAssetStudio/
- https://easyappicon.com/
- https://appicon.co/

These tools will generate all required sizes automatically.

## Current Icon

The current placeholder icon shows a notebook/document symbol in purple/white colors matching the Material Design theme.

## Tips

- Use a simple, recognizable design
- Ensure icon looks good on both light and dark backgrounds
- Test on different Android versions and launchers
- Follow Material Design icon guidelines
- Avoid text in icons (doesn't scale well)

## Adaptive Icons (Android 8.0+)

Modern Android uses adaptive icons with:
- **Background layer**: Solid color or pattern
- **Foreground layer**: Your actual icon

The system can mask these into different shapes (circle, square, rounded square, etc.)

Current setup:
- Background: Purple (`#6750A4`)
- Foreground: White notebook icon

You can customize these in:
- Background: `res/values/ic_launcher_background.xml`
- Foreground: `res/drawable/ic_launcher_foreground.xml`

