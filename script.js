class NotesApp {
    constructor() {
        this.notes = [];
        this.init();
    }

    init() {
        this.bindEvents();
        this.fetchNotes();
    }

    bindEvents() {
        const form = document.getElementById('noteForm');
        form.addEventListener('submit', (e) => this.handleSubmit(e));
    }

    async handleSubmit(e) {
        e.preventDefault();
        const title = document.getElementById('noteTitle').value.trim();
        const content = document.getElementById('noteContent').value.trim();
        if (!title || !content) return;

        try {
            const userId = await getUserId();
            if (!userId) {
                alert('Please sign in to save notes.');
                return;
            }
            const res = await fetch('/api/notes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title, content }),
                headers: {
                    'Content-Type': 'application/json',
                    'X-User-Id': userId
                }
            });
            const data = await res.json();
            if (!res.ok) throw new Error(data.error || 'Failed to save note');
            this.notes.unshift({
                id: data.note.id,
                title: data.note.title,
                content: data.note.content,
                created_at: data.note.created_at
            });
            document.getElementById('noteForm').reset();
            this.renderNotes();
        } catch (err) {
            alert('Error saving note: ' + err.message);
        }
    }

    async fetchNotes() {
        try {
            const userId = await getUserId();
            if (!userId) {
                // Not signed in yet
                document.getElementById('notesList').innerHTML = '<div class="empty-state">Sign in to view your notes.</div>';
                return;
            }
            const res = await fetch('/api/notes', { headers: { 'X-User-Id': userId } });
            const data = await res.json();
            if (!res.ok) throw new Error(data.error || 'Failed to load notes');
            this.notes = data.notes || [];
            this.renderNotes();
        } catch (err) {
            const notesList = document.getElementById('notesList');
            notesList.innerHTML = `<div class="empty-state">Failed to load notes: ${this.escapeHtml(err.message)}</div>`;
        }
    }

    renderNotes() {
        const notesList = document.getElementById('notesList');
        if (!this.notes || this.notes.length === 0) {
            notesList.innerHTML = '<div class="empty-state">No notes yet. Create your first note above!</div>';
            return;
        }
        notesList.innerHTML = this.notes.map(note => `
            <div class="note-item">
                <div class="note-title">${this.escapeHtml(note.title)}</div>
                <div class="note-content">${this.escapeHtml(note.content)}</div>
                <div class="note-date">Created: ${new Date(note.created_at).toLocaleString()}</div>
            </div>
        `).join('');
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new NotesApp();
});
