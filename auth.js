let firebaseApp;
let firebaseAuth;
let currentUser = null;

async function initFirebase() {
    try {
        const res = await fetch('/config');
        const cfg = await res.json();
        const config = cfg.firebase || {};
        firebaseApp = firebase.initializeApp(config);
        firebaseAuth = firebase.auth();
        firebaseAuth.onAuthStateChanged(async (user) => {
            currentUser = user;
            const header = document.querySelector('header h1');
            if (user) {
                header.innerHTML = '📝 My Notes — ' + (user.displayName || 'Signed in');
                document.getElementById('authActions').innerHTML = '<button id="signOutBtn">Sign out</button>';
                document.getElementById('signOutBtn').onclick = signOut;
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
                document.getElementById('authActions').innerHTML = '<button id="signInBtn">Sign in with Google</button>';
                document.getElementById('signInBtn').onclick = signInWithGoogle;
                document.dispatchEvent(new CustomEvent('auth-signed-out'));
            }
        });
    } catch (e) {
        console.error('Failed to init Firebase', e);
    }
}

async function signInWithGoogle() {
    const provider = new firebase.auth.GoogleAuthProvider();
    await firebaseAuth.signInWithPopup(provider);
}

async function signOut() {
    await firebaseAuth.signOut();
}

async function getUserId() {
    return currentUser ? currentUser.uid : null;
}

document.addEventListener('DOMContentLoaded', initFirebase);


