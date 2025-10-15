class NotesApp {
    constructor() {
        this.notes = this.loadNotes();
        this.init();
    }

    init() {
        this.bindEvents();
        this.renderNotes();
    }

    bindEvents() {
        const form = document.getElementById('noteForm');
        form.addEventListener('submit', (e) => this.handleSubmit(e));
    }

    handleSubmit(e) {
        e.preventDefault();
        
        const title = document.getElementById('noteTitle').value.trim();
        const content = document.getElementById('noteContent').value.trim();
        
        if (title && content) {
            this.addNote(title, content);
            document.getElementById('noteForm').reset();
        }
    }

    addNote(title, content) {
        const note = {
            id: Date.now().toString(),
            title,
            content,
            createdAt: new Date().toISOString()
        };
        
        this.notes.unshift(note);
        this.saveNotes();
        this.renderNotes();
    }

    renderNotes() {
        const notesList = document.getElementById('notesList');
        
        if (this.notes.length === 0) {
            notesList.innerHTML = '<div class="empty-state">No notes yet. Create your first note above!</div>';
            return;
        }
        
        notesList.innerHTML = this.notes.map(note => `
            <div class="note-item">
                <div class="note-title">${this.escapeHtml(note.title)}</div>
                <div class="note-content">${this.escapeHtml(note.content)}</div>
                <div class="note-date">Created: ${new Date(note.createdAt).toLocaleString()}</div>
            </div>
        `).join('');
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    saveNotes() {
        localStorage.setItem('notes', JSON.stringify(this.notes));
    }

    loadNotes() {
        const saved = localStorage.getItem('notes');
        return saved ? JSON.parse(saved) : [];
    }
}

// Initialize the app when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new NotesApp();
});
