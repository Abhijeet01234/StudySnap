package com.studysnap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long noteId;              // 📝 ID of the note this comment belongs to
    private String userEmail;         // 📧 Who commented
    private String content;           // 💬 Actual comment
    private LocalDateTime createdAt;  // 🕒 Time of comment

    public Comment() {}

    public Comment(String content, String userEmail, Long noteId, LocalDateTime createdAt) {
        this.content = content;
        this.userEmail = userEmail;
        this.noteId = noteId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", noteId=" + noteId +
                ", createdAt=" + createdAt +
                '}';
    }
}
