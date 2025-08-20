package com.studysnap.controller;

import com.studysnap.model.Note;
import com.studysnap.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private NoteService noteService;

    // 🛡️ Secured dashboard endpoint (test only)
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }

    // 🔍 Get notes by status (PENDING / APPROVED / REJECTED)
    @GetMapping("/notes/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Note> getNotesByStatus(@PathVariable String status) {
        return noteService.getNotesByStatus(status.toUpperCase());
    }

    // ✅ Approve a note
    @PutMapping("/notes/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveNote(@PathVariable Long id) {
        return noteService.updateNoteStatus(id, "APPROVED");
    }

    // ❌ Reject a note
    @PutMapping("/notes/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectNote(@PathVariable Long id) {
        return noteService.updateNoteStatus(id, "REJECTED");
    }

    // 🔄 Revert to PENDING
    @PutMapping("/notes/pending/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String revertToPending(@PathVariable Long id) {
        return noteService.updateNoteStatus(id, "PENDING");
    }
}
