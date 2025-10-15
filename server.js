const express = require('express');
const path = require('path');
const { createClient } = require('@supabase/supabase-js');

const app = express();
const PORT = process.env.PORT || 3000;

// Parse JSON bodies
app.use(express.json());

// Supabase client (uses anon key only per env constraints)
const SUPABASE_URL = process.env.SUPABASE_URL;
const SUPABASE_ANON_KEY = process.env.SUPABASE_ANON_KEY;

const supabase = SUPABASE_URL && SUPABASE_ANON_KEY
  ? createClient(SUPABASE_URL, SUPABASE_ANON_KEY)
  : null;

// Serve static files from the current directory
app.use(express.static('.'));

// Route for the main page
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

// Ensure Firebase redirect handler path doesn't 404 (serve SPA entry)
app.get('/__/auth/handler', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

// Support Firebase Auth redirect handler path when not on Firebase Hosting
// No special handler needed for popup flow

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

// API: Get notes (expects X-User-Id header from client)
app.get('/api/notes', async (req, res) => {
    if (!supabase) {
        return res.status(500).json({ error: 'Supabase is not configured on the server.' });
    }
    const userId = req.headers['x-user-id'];
    if (!userId) return res.status(401).json({ error: 'Missing X-User-Id' });
    const { data, error } = await supabase
        .from('notes')
        .select('*')
        .eq('user_id', userId)
        .order('created_at', { ascending: false });
    if (error) {
        return res.status(500).json({ error: error.message });
    }
    return res.json({ notes: data });
});

// API: Create note (expects X-User-Id header from client)
app.post('/api/notes', async (req, res) => {
    if (!supabase) {
        return res.status(500).json({ error: 'Supabase is not configured on the server.' });
    }
    const userId = req.headers['x-user-id'];
    if (!userId) return res.status(401).json({ error: 'Missing X-User-Id' });
    const { title, content } = req.body || {};
    if (!title || !content) {
        return res.status(400).json({ error: 'Title and content are required.' });
    }
    const insertPayload = { title, content, user_id: userId };
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

// API: Upsert user profile
app.post('/api/users/upsert', async (req, res) => {
    if (!supabase) {
        return res.status(500).json({ error: 'Supabase is not configured on the server.' });
    }
    const { id, email, display_name, photo_url } = req.body || {};
    if (!id) return res.status(400).json({ error: 'Missing id' });
    const { data, error } = await supabase
        .from('users')
        .upsert({ id, email, display_name, photo_url })
        .select()
        .single();
    if (error) return res.status(500).json({ error: error.message });
    return res.json({ user: data });
});

// Start the server
app.listen(PORT, () => {
    console.log(`Notes App is running on port ${PORT}`);
});
