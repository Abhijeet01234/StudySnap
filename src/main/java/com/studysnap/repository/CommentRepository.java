package com.studysnap.repository;

import com.studysnap.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 🔍 Get all comments for a specific note
    List<Comment> findByNoteId(Long noteId);

    // Optional: Get all comments by a specific user
    List<Comment> findByUserEmail(String email);
}
