const express = require('express');
const path = require('path');
const { createClient } = require('@supabase/supabase-js');
const admin = require('firebase-admin');

const app = express();
const PORT = process.env.PORT || 3000;

// Parse JSON bodies
app.use(express.json());

// Supabase client (prefer service role for server-side writes if provided)
const SUPABASE_URL = process.env.SUPABASE_URL;
const SUPABASE_SERVICE_ROLE_KEY = process.env.SUPABASE_SERVICE_ROLE_KEY;
const SUPABASE_ANON_KEY = process.env.SUPABASE_ANON_KEY;

const supabaseKey = SUPABASE_SERVICE_ROLE_KEY || SUPABASE_ANON_KEY;
const supabase = SUPABASE_URL && supabaseKey
  ? createClient(SUPABASE_URL, supabaseKey)
  : null;

// Firebase Admin init for token verification
const FIREBASE_PROJECT_ID = process.env.FIREBASE_PROJECT_ID;
const FIREBASE_CLIENT_EMAIL = process.env.FIREBASE_CLIENT_EMAIL;
let FIREBASE_PRIVATE_KEY = process.env.FIREBASE_PRIVATE_KEY;
if (FIREBASE_PRIVATE_KEY && FIREBASE_PRIVATE_KEY.includes('\\n')) {
    FIREBASE_PRIVATE_KEY = FIREBASE_PRIVATE_KEY.replace(/\\n/g, '\n');
}
if (!admin.apps.length && FIREBASE_PROJECT_ID && FIREBASE_CLIENT_EMAIL && FIREBASE_PRIVATE_KEY) {
    admin.initializeApp({
        credential: admin.credential.cert({
            projectId: FIREBASE_PROJECT_ID,
            clientEmail: FIREBASE_CLIENT_EMAIL,
            privateKey: FIREBASE_PRIVATE_KEY
        })
    });
}

// Serve static files from the current directory
app.use(express.static('.'));

// Route for the main page
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

// Public config for Firebase client
app.get('/config', (req, res) => {
    res.json({
        firebase: {
            apiKey: process.env.FIREBASE_API_KEY || '',
            authDomain: process.env.FIREBASE_AUTH_DOMAIN || '',
            projectId: process.env.FIREBASE_PROJECT_ID || ''
        }
    });
});

// Auth middleware (expects Authorization: Bearer <firebase_id_token>)
async function requireAuth(req, res, next) {
    try {
        const authHeader = req.headers.authorization || '';
        const token = authHeader.startsWith('Bearer ')
            ? authHeader.slice('Bearer '.length)
            : null;
        if (!token) return res.status(401).json({ error: 'Missing token' });
        if (!admin.apps.length) return res.status(500).json({ error: 'Auth not configured' });
        const decoded = await admin.auth().verifyIdToken(token);
        req.userId = decoded.uid;
        next();
    } catch (err) {
        return res.status(401).json({ error: 'Invalid or expired token' });
    }
}

// API: Get notes (scoped to authenticated user)
app.get('/api/notes', requireAuth, async (req, res) => {
    if (!supabase) {
        return res.status(500).json({ error: 'Supabase is not configured on the server.' });
    }
    const { data, error } = await supabase
        .from('notes')
        .select('*')
        .eq('user_id', req.userId)
        .order('created_at', { ascending: false });
    if (error) {
        return res.status(500).json({ error: error.message });
    }
    return res.json({ notes: data });
});

// API: Create note (scoped to authenticated user)
app.post('/api/notes', requireAuth, async (req, res) => {
    if (!supabase) {
        return res.status(500).json({ error: 'Supabase is not configured on the server.' });
    }
    const { title, content } = req.body || {};
    if (!title || !content) {
        return res.status(400).json({ error: 'Title and content are required.' });
    }
    const insertPayload = { title, content, user_id: req.userId };
    const { data, error } = await supabase
        .from('notes')
        .insert(insertPayload)
        .select()
        .single();
    if (error) {
        return res.status(500).json({ error: error.message });
    }
    return res.status(201).json({ note: data });
});

// Start the server
app.listen(PORT, () => {
    console.log(`Notes App is running on port ${PORT}`);
});
