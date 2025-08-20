package com.studysnap.service;

import com.studysnap.model.Comment;
import com.studysnap.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    

    // ✅ Save a new comment
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // ➕ Add a new comment
    public Comment addComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    // 🔍 Get all comments for a note
    public List<Comment> getCommentsByNoteId(Long noteId) {
        return commentRepository.findByNoteId(noteId);
    }

    // 🧑 Optional: Get all comments by user
    public List<Comment> getCommentsByUserEmail(String email) {
        return commentRepository.findByUserEmail(email);
    }

    // ❌ Delete a comment by ID (admin)
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
