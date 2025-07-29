package org.example.user.Controller;

import org.example.user.Dto.NoteRequest;
import org.example.user.Entity.Note;
import org.example.user.Service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Note> createNote(@PathVariable Long userId, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(noteService.createNote(userId, request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Note>> getNotes(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "all") String type) {
        return ResponseEntity.ok(noteService.getUserNotes(userId, type));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Note> updateNote(@PathVariable Long noteId, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(noteService.updateNote(noteId, request));
    }

}
