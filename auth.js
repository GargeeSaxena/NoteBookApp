let firebaseApp;
let firebaseAuth;
let currentUser = null;

async function initFirebase() {
    try {
        const res = await fetch('/config');
        const cfg = await res.json();
        const config = cfg.firebase || {};
        if (!config.apiKey || !config.authDomain || !config.projectId) {
            const el = document.getElementById('authError');
            if (el) {
                el.style.display = '';
                el.textContent = 'Missing Firebase config. Check env vars on Render.';
            }
            return;
        }
        firebaseApp = firebase.initializeApp(config);
        firebaseAuth = firebase.auth();

        // If we're on Firebase redirect handler, finalize and go back to root
        try {
            if (window.location && typeof window.location.pathname === 'string' && window.location.pathname.indexOf('/__/auth/handler') === 0) {
                await firebaseAuth.getRedirectResult();
                window.location.replace('/');
                return; // stop further init on handler page
            }
        } catch (err) {
            const el = document.getElementById('authError');
            if (el) {
                el.style.display = '';
                el.textContent = (err && err.message) || 'Authentication failed';
            }
        }

        // Ensure button is wired on first load
        const btn = document.getElementById('signInBtn');
        if (btn) btn.onclick = signInWithGoogle;

        firebaseAuth.onAuthStateChanged(async (user) => {
            currentUser = user;
            const header = document.querySelector('header h1');
            if (user) {
                header.innerHTML = '📝 My Notes — ' + (user.displayName || 'Signed in');
                document.getElementById('authActions').innerHTML = '<button id="signOutBtn">Sign out</button>';
                document.getElementById('signOutBtn').onclick = signOut;
                document.getElementById('authSection').style.display = 'none';
                // Upsert user profile in backend
                try {
                    await fetch('/api/users/upsert', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            id: user.uid,
                            email: user.email || null,
                            display_name: user.displayName || null,
                            photo_url: user.photoURL || null
                        })
                    });
                } catch (e) { console.warn('User upsert failed', e); }
                // Trigger notes fetch after sign-in
                document.dispatchEvent(new CustomEvent('auth-signed-in'));
            } else {
                header.innerHTML = '📝 My Notes';
                document.getElementById('authActions').innerHTML = '';
                const btn = document.getElementById('signInBtn');
                if (btn) btn.onclick = signInWithGoogle;
                document.getElementById('authSection').style.display = '';
                document.dispatchEvent(new CustomEvent('auth-signed-out'));
            }
        });
    } catch (e) {
        console.error('Failed to init Firebase', e);
    }
}

async function signInWithGoogle() {
    const provider = new firebase.auth.GoogleAuthProvider();
    try {
        await firebaseAuth.signInWithRedirect(provider);
    } catch (err) {
        const el = document.getElementById('authError');
        if (el) {
            el.style.display = '';
            el.textContent = (err && err.message) || 'Authentication failed';
        }
    }
}

async function signOut() {
    await firebaseAuth.signOut();
}

async function getUserId() {
    return currentUser ? currentUser.uid : null;
}

document.addEventListener('DOMContentLoaded', initFirebase);


