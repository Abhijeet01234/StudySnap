package com.studysnap.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.studysnap.model.Note;
import com.studysnap.service.NoteService;

@RestController
@RequestMapping("/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    // 🚀 Search notes by keyword
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Note>> searchNotes(@RequestParam("keyword") String keyword) {
        List<Note> result = noteService.searchNotes(keyword);
        return ResponseEntity.ok(result);
    }

    // 🎯 Filter notes by status
    @GetMapping("/filter")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Note>> filterByStatus(
            @RequestParam("status") String status,
            @RequestParam(value = "uploadedBy", required = false) String uploadedBy) {

        List<Note> result = (uploadedBy != null)
                ? noteService.getNotesByUploaderAndStatus(uploadedBy, status)
                : noteService.getNotesByStatus(status);

        return ResponseEntity.ok(result);
    }

    // 📝 Upload note with metadata only
    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Note> uploadNote(@RequestBody Note note) {
        note.setUploadedAt(LocalDateTime.now());
        Note saved = noteService.saveNote(note);
        return ResponseEntity.ok(saved);
    }

    // 🔍 Search with pagination
    @GetMapping("/search/paginated")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> searchNotesPaginated(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "uploadedAt") String sortBy) {
        return ResponseEntity.ok(noteService.searchNotesPaginated(keyword, page, size, sortBy));
    }

    // 🏷️ Search by Tag with pagination
    @GetMapping("/tag/paginated")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getNotesByTagPaginated(
            @RequestParam("tag") String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "uploadedAt") String sortBy) {
        return ResponseEntity.ok(noteService.getNotesByTagPaginated(tag, page, size, sortBy));
    }

    // 📤 Upload note with file (multipart)
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> uploadNoteWithFile(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("uploadedBy") String uploadedBy,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "tags", required = false) List<String> tags) {

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String filePath = uploadDir + "/" + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            Note note = new Note();
            note.setTitle(title);
            note.setDescription(description);
            note.setUploadedBy(uploadedBy);
            note.setFileName(file.getOriginalFilename());
            note.setUploadedAt(LocalDateTime.now());
            note.setTags(tags); // ✅ Set tags

            noteService.saveNote(note);

            return ResponseEntity.ok("Note uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    // 📄 Get all notes (non-paginated, legacy support)
    @GetMapping("/all")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    // 📄 Get all notes (paginated)
    @GetMapping("/all/paginated")
    public ResponseEntity<?> getAllNotesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "uploadedAt") String sortBy) {
        return ResponseEntity.ok(noteService.searchNotesPaginated("", page, size, sortBy));
    }

    // 👤 Get notes uploaded by specific user (non-paginated)
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Note>> getNotesByUploader(@PathVariable String email) {
        return ResponseEntity.ok(noteService.getNotesByUploader(email));
    }

    // 👤 Get notes uploaded by specific user (paginated)
    @GetMapping("/user/{email}/paginated")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getNotesByUploaderPaginated(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "uploadedAt") String sortBy) {
        return ResponseEntity.ok(noteService.getNotesByUploaderPaginated(email, page, size, sortBy));
    }

    // ❌ Delete note (ADMIN only)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ResponseEntity.ok("Note deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting note: " + e.getMessage());
        }
    }

    // 📥 Download note file
    @GetMapping("/download/{fileName}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Resource> downloadNoteFile(@PathVariable String fileName) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads";
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 🔍 Get notes by single tag
    @GetMapping("/tag")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Note>> getNotesByTag(@RequestParam("tag") String tag) {
        List<Note> notes = noteService.getNotesByTag(tag);
        return ResponseEntity.ok(notes);
    }

    // 📌 Get all distinct tags
    @GetMapping("/tags")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<String>> getAllTags() {
        List<String> tags = noteService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    // 🔍 Search notes by multiple tags (comma-separated)
    @GetMapping("/tags/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Note>> getNotesByMultipleTags(@RequestParam("tags") String tags) {
        List<String> tagList = Arrays.stream(tags.split(","))
                                     .map(String::trim)
                                     .collect(Collectors.toList());
        List<Note> notes = noteService.getNotesByMultipleTags(tagList);
        return ResponseEntity.ok(notes);
    }
}
