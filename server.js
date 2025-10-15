const express = require('express');
const path = require('path');
const { createClient } = require('@supabase/supabase-js');

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

// Serve static files from the current directory
app.use(express.static('.'));

// Route for the main page
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

// API: Get notes
app.get('/api/notes', async (req, res) => {
    if (!supabase) {
        return res.status(500).json({ error: 'Supabase is not configured on the server.' });
    }
    const { data, error } = await supabase
        .from('notes')
        .select('*')
        .order('created_at', { ascending: false });
    if (error) {
        return res.status(500).json({ error: error.message });
    }
    return res.json({ notes: data });
});

// API: Create note
app.post('/api/notes', async (req, res) => {
    if (!supabase) {
        return res.status(500).json({ error: 'Supabase is not configured on the server.' });
    }
    const { title, content } = req.body || {};
    if (!title || !content) {
        return res.status(400).json({ error: 'Title and content are required.' });
    }
    const insertPayload = { title, content };
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
