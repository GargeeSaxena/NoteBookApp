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
        firebaseAuth.onAuthStateChanged((user) => {
            currentUser = user;
            const header = document.querySelector('header h1');
            if (user) {
                header.innerHTML = '📝 My Notes — ' + (user.displayName || 'Signed in');
                document.getElementById('authActions').innerHTML = '<button id="signOutBtn">Sign out</button>';
                document.getElementById('signOutBtn').onclick = signOut;
            } else {
                header.innerHTML = '📝 My Notes';
                document.getElementById('authActions').innerHTML = '<button id="signInBtn">Sign in with Google</button>';
                document.getElementById('signInBtn').onclick = signInWithGoogle;
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


