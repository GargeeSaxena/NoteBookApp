let supabaseClient;
let currentUser = null;

async function initAuth() {
    try {
        const res = await fetch('/config');
        const cfg = await res.json();
        
        if (!cfg.supabase || !cfg.supabase.url || !cfg.supabase.anonKey) {
            showError('Missing Supabase configuration. Please check environment variables.');
            return;
        }

        // Initialize Supabase client
        supabaseClient = supabase.createClient(cfg.supabase.url, cfg.supabase.anonKey);
        
        // Check for existing session
        const { data: { session } } = await supabaseClient.auth.getSession();
        
        if (session) {
            currentUser = session.user;
            handleAuthStateChange(session.user);
        }

        // Listen for auth state changes
        supabaseClient.auth.onAuthStateChange((event, session) => {
            currentUser = session?.user || null;
            handleAuthStateChange(currentUser);
        });

        // Wire up sign-in button
        const signInBtn = document.getElementById('signInBtn');
        if (signInBtn) {
            signInBtn.onclick = signInWithGoogle;
        }
    } catch (e) {
        console.error('Failed to initialize authentication:', e);
        showError('Authentication initialization failed: ' + e.message);
    }
}

async function signInWithGoogle() {
    try {
        const { error } = await supabaseClient.auth.signInWithOAuth({
            provider: 'google',
            options: {
                redirectTo: window.location.origin
            }
        });
        
        if (error) throw error;
    } catch (err) {
        console.error('Sign in error:', err);
        showError('Sign in failed: ' + err.message);
    }
}

async function signOut() {
    try {
        const { error } = await supabaseClient.auth.signOut();
        if (error) throw error;
    } catch (err) {
        console.error('Sign out error:', err);
        showError('Sign out failed: ' + err.message);
    }
}

function handleAuthStateChange(user) {
    const header = document.querySelector('header h1');
    const authActions = document.getElementById('authActions');
    const authSection = document.getElementById('authSection');
    
    if (user) {
        // User is signed in
        const displayName = user.user_metadata?.full_name || user.email?.split('@')[0] || 'User';
        header.innerHTML = '📝 My Notes — ' + displayName;
        authActions.innerHTML = '<button id="signOutBtn">Sign out</button>';
        document.getElementById('signOutBtn').onclick = signOut;
        authSection.style.display = 'none';
        
        // Upsert user profile in backend
        fetch('/api/users/upsert', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id: user.id,
                email: user.email || null,
                display_name: user.user_metadata?.full_name || null,
                photo_url: user.user_metadata?.avatar_url || null
            })
        }).catch(e => console.warn('User upsert failed', e));
        
        // Trigger notes fetch after sign-in
        document.dispatchEvent(new CustomEvent('auth-signed-in'));
    } else {
        // User is signed out
        header.innerHTML = '📝 My Notes';
        authActions.innerHTML = '';
        authSection.style.display = '';
        
        // Trigger sign-out event
        document.dispatchEvent(new CustomEvent('auth-signed-out'));
    }
}

function showError(message) {
    const errorEl = document.getElementById('authError');
    if (errorEl) {
        errorEl.style.display = 'block';
        errorEl.textContent = message;
    }
}

async function getUserId() {
    return currentUser ? currentUser.id : null;
}

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initAuth);
} else {
    initAuth();
}
