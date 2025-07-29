package org.example.user.Repo;

import org.example.user.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long userId);
    List<Note> findByUserIdAndType(Long userId, String type);
}

