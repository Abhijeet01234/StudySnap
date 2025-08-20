package com.studysnap.controller;

import com.studysnap.model.Comment;
import com.studysnap.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 📝 1. Add a comment to a note
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }

    // 📄 2. Get all comments for a specific note
    @GetMapping("/note/{noteId}")
    public ResponseEntity<List<Comment>> getCommentsByNoteId(@PathVariable Long noteId) {
        List<Comment> comments = commentService.getCommentsByNoteId(noteId);
        return ResponseEntity.ok(comments);
    }

    // ❌ 3. Delete a comment (admin only)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully.");
    }
}

