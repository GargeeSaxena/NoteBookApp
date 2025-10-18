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

        // Wire up sign-in and sign-up buttons
        const signInBtn = document.getElementById('signInBtn');
        const signUpBtn = document.getElementById('signUpBtn');
        
        if (signInBtn) {
            signInBtn.onclick = signIn;
        }
        if (signUpBtn) {
            signUpBtn.onclick = signUp;
        }

        // Allow Enter key to sign in
        const emailInput = document.getElementById('authEmail');
        const passwordInput = document.getElementById('authPassword');
        
        if (emailInput && passwordInput) {
            [emailInput, passwordInput].forEach(input => {
                input.addEventListener('keypress', (e) => {
                    if (e.key === 'Enter') {
                        signIn();
                    }
                });
            });
        }
    } catch (e) {
        console.error('Failed to initialize authentication:', e);
        showError('Authentication initialization failed: ' + e.message);
    }
}

async function signUp() {
    try {
        const email = document.getElementById('authEmail').value.trim();
        const password = document.getElementById('authPassword').value;

        if (!email || !password) {
            showError('Please enter both email and password');
            return;
        }

        if (password.length < 6) {
            showError('Password must be at least 6 characters');
            return;
        }

        // Disable buttons during sign up
        const signInBtn = document.getElementById('signInBtn');
        const signUpBtn = document.getElementById('signUpBtn');
        signInBtn.disabled = true;
        signUpBtn.disabled = true;
        signUpBtn.textContent = 'Creating...';

        const { data, error } = await supabaseClient.auth.signUp({
            email: email,
            password: password,
            options: {
                emailRedirectTo: window.location.origin
            }
        });
        
        if (error) throw error;

        // Check if email confirmation is required
        if (data.user && !data.session) {
            showSuccess('Account created! Please check your email to confirm your account.');
        } else {
            showSuccess('Account created successfully! You can now use the app.');
        }
        
        // Clear form
        document.getElementById('authEmail').value = '';
        document.getElementById('authPassword').value = '';
        
    } catch (err) {
        console.error('Sign up error:', err);
        showError('Sign up failed: ' + err.message);
    } finally {
        // Re-enable buttons
        const signInBtn = document.getElementById('signInBtn');
        const signUpBtn = document.getElementById('signUpBtn');
        if (signInBtn) signInBtn.disabled = false;
        if (signUpBtn) {
            signUpBtn.disabled = false;
            signUpBtn.textContent = 'Sign Up';
        }
    }
}

async function signIn() {
    try {
        const email = document.getElementById('authEmail').value.trim();
        const password = document.getElementById('authPassword').value;

        if (!email || !password) {
            showError('Please enter both email and password');
            return;
        }

        // Disable buttons during sign in
        const signInBtn = document.getElementById('signInBtn');
        const signUpBtn = document.getElementById('signUpBtn');
        signInBtn.disabled = true;
        signUpBtn.disabled = true;
        signInBtn.textContent = 'Signing in...';

        const { data, error } = await supabaseClient.auth.signInWithPassword({
            email: email,
            password: password
        });
        
        if (error) throw error;

        showSuccess('Signed in successfully!');
        
        // Clear form
        document.getElementById('authEmail').value = '';
        document.getElementById('authPassword').value = '';
        
    } catch (err) {
        console.error('Sign in error:', err);
        showError('Sign in failed: ' + err.message);
    } finally {
        // Re-enable buttons
        const signInBtn = document.getElementById('signInBtn');
        const signUpBtn = document.getElementById('signUpBtn');
        if (signInBtn) {
            signInBtn.disabled = false;
            signInBtn.textContent = 'Sign In';
        }
        if (signUpBtn) signUpBtn.disabled = false;
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
        authActions.innerHTML = '<button id="signOutBtn" style="background:#EA4335;color:#fff;border:none;padding:8px 16px;border-radius:4px;cursor:pointer;">Sign out</button>';
        document.getElementById('signOutBtn').onclick = signOut;
        authSection.style.display = 'none';
        
        // Clear any error messages
        hideError();
        hideSuccess();
        
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
    const successEl = document.getElementById('authSuccess');
    if (successEl) successEl.style.display = 'none';
    if (errorEl) {
        errorEl.style.display = 'block';
        errorEl.textContent = message;
    }
}

function hideError() {
    const errorEl = document.getElementById('authError');
    if (errorEl) {
        errorEl.style.display = 'none';
        errorEl.textContent = '';
    }
}

function showSuccess(message) {
    const successEl = document.getElementById('authSuccess');
    const errorEl = document.getElementById('authError');
    if (errorEl) errorEl.style.display = 'none';
    if (successEl) {
        successEl.style.display = 'block';
        successEl.textContent = message;
        // Hide after 3 seconds
        setTimeout(() => {
            if (successEl) successEl.style.display = 'none';
        }, 3000);
    }
}

function hideSuccess() {
    const successEl = document.getElementById('authSuccess');
    if (successEl) {
        successEl.style.display = 'none';
        successEl.textContent = '';
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
