# 📝 My Notes - Notebook App

A beautiful, modern notes application with Google authentication powered by Supabase, available as both a web app and Android client.

## 🌟 Features

### Web App
- ✅ Google Sign-in (Simple & Secure)
- ✅ Create and view notes
- ✅ User-specific notes (each user sees only their notes)
- ✅ Beautiful, modern UI
- ✅ Real-time updates
- ✅ Deployed on Render with Supabase backend

### Android Client
- ✅ Email/Password authentication via Supabase
- ✅ Full CRUD operations for notes
- ✅ File attachment support with Supabase Storage
- ✅ Premium status display
- ✅ Material Design 3 with dark theme
- ✅ Modern MVVM architecture

## 🚀 Recent Updates

**Authentication has been rebuilt** for simplicity and reliability:
- Switched from Firebase to Supabase Auth
- Eliminated complex redirect flows
- Simpler code, easier to maintain
- More reliable sign-in experience

## 📋 Setup Instructions

### Prerequisites

- Node.js 14 or higher
- A Supabase account (free tier is fine)
- A Google Cloud account for OAuth credentials

### 1. Install Dependencies

```bash
npm install
```

### 2. Configure Environment Variables

Create a `.env` file (already done if you followed the fix):

```env
SUPABASE_URL=https://mkxytkpewzmaucgwhahs.supabase.co
SUPABASE_ANON_KEY=your-supabase-anon-key
PORT=3000
```

### 3. Set Up Database

1. Go to your Supabase dashboard
2. Open SQL Editor
3. Run the contents of `schema.sql`

This will create:
- `notes` table
- `users` table
- Proper indexes
- Row Level Security policies

### 4. Configure Google OAuth

**This is crucial for authentication to work!**

1. **In Supabase:**
   - Go to Authentication → Providers
   - Enable Google provider
   - Note the callback URL: `https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`

2. **In Google Cloud Console:**
   - Go to https://console.cloud.google.com/apis/credentials
   - Create a new project (or select existing)
   - Create OAuth 2.0 Client ID
   - Application type: Web application
   - Add Authorized redirect URI: `https://mkxytkpewzmaucgwhahs.supabase.co/auth/v1/callback`
   - Copy the Client ID and Client Secret

3. **Back in Supabase:**
   - Paste Client ID and Client Secret
   - Save

### 5. Run Locally

```bash
npm start
```

Open http://localhost:3000 in your browser.

### 6. Deploy to Render

1. Push your code to GitHub
2. In Render dashboard:
   - Create a new Web Service
   - Connect your GitHub repository
   - Set environment variables:
     - `SUPABASE_URL`
     - `SUPABASE_ANON_KEY`
3. Deploy!

## 📁 Project Structure

```
NoteBook App/
├── android/                 # Android client (Kotlin)
│   ├── app/                 # Android app module
│   ├── ANDROID_SETUP_GUIDE.md  # Detailed Android setup
│   └── README.md            # Android quick start
├── migrations/              # Database migrations
├── index.html               # Web app main page
├── styles.css               # Web app styling
├── auth.js                  # Web authentication logic
├── script.js                # Web notes logic
├── server.js                # Express server
├── schema.sql               # Database schema
├── package.json             # Node.js dependencies
├── .env                     # Environment variables (local)
├── README.md                # This file
├── QUICK_START.md           # Quick setup guide
└── SETUP_AUTH.md            # Detailed auth setup
```

## 🛠 Technologies Used

### Web App
- **Frontend:** Vanilla JavaScript, HTML5, CSS3
- **Backend:** Node.js, Express
- **Database:** Supabase (PostgreSQL)
- **Authentication:** Supabase Auth with Google OAuth
- **Hosting:** Render

### Android Client
- **Language:** Kotlin
- **Architecture:** MVVM
- **UI:** Material Design 3, XML layouts, ViewBinding
- **Backend:** Supabase (Auth, Database, Storage)
- **Async:** Kotlin Coroutines
- **Min SDK:** 24 (Android 7.0), **Target SDK:** 35 (Android 15)

## 🔒 Security

- User authentication via Supabase Auth
- Row Level Security (RLS) enabled on database
- User-specific data isolation
- HTTPS on production

## 📱 Usage

1. **Sign In:** Click "Sign in / Sign up with Google"
2. **Create Notes:** Use the form to create new notes
3. **View Notes:** All your notes are displayed below the form
4. **Sign Out:** Click the "Sign out" button

## 🐛 Troubleshooting

### Sign-in button doesn't work
- Make sure Google OAuth is configured in Supabase
- Check browser console for errors
- Verify environment variables are set

### Notes don't load
- Ensure database schema is properly set up
- Check that environment variables are correct
- Verify RLS policies are enabled

### Server won't start
- Check that all dependencies are installed: `npm install`
- Verify `.env` file exists and has correct values
- Make sure port 3000 is not already in use

## 📚 Documentation

### Web App
- **QUICK_START.md** - Get started in 5 minutes
- **SETUP_AUTH.md** - Detailed authentication setup guide
- **schema.sql** - Database schema and comments

### Android Client
- **android/README.md** - Android quick start guide
- **android/ANDROID_SETUP_GUIDE.md** - Comprehensive Android setup and troubleshooting
- **android/local.properties.example** - Configuration template

## 👨‍💻 Author

**GargeeSaxena**

## 📄 License

MIT License - Feel free to use this project for learning or personal use.

## 🤝 Contributing

This is a personal project, but feel free to fork and modify for your own use!

## 📞 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review `QUICK_START.md` and `SETUP_AUTH.md`
3. Check browser console for error messages
4. Verify all environment variables are set correctly

---

**Note:** This project uses Supabase's anon key which is safe to expose publicly. Row Level Security (RLS) protects your data.

