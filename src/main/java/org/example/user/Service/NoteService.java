package org.example.user.Service;

import org.example.user.Dto.NoteRequest;
import org.example.user.Entity.Note;
import org.example.user.Entity.User;
import org.example.user.Repo.NoteRepository;
import org.example.user.Repo.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Note createNote(Long userId, NoteRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setType(request.getType());
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setUser(user);

        return noteRepository.save(note);
    }

    public void deleteNote(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    public List<Note> getUserNotes(Long userId, String type) {
        if (type.equalsIgnoreCase("all")) {
            return noteRepository.findByUserId(userId);
        }
        return noteRepository.findByUserIdAndType(userId, type);
    }

    public Note updateNote(Long noteId, NoteRequest request) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setType(request.getType());
        note.setUpdatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }



}

