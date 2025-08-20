package com.studysnap.service;

import com.studysnap.model.Note;
import org.springframework.data.domain.Page;
import java.util.List;

public interface NoteService {

    Note saveNote(Note note);

    List<Note> getAllNotes();

    List<Note> getNotesByUploader(String email);

    void deleteNote(Long id);

    List<Note> getNotesByStatus(String status);

    List<Note> getNotesByUploaderAndStatus(String uploadedBy, String status);

    String updateNoteStatus(Long id, String status);

    List<Note> searchNotes(String keyword);

    List<Note> getNotesByTag(String tag);

    // ✅ New
    List<String> getAllTags();

    List<Note> getNotesByMultipleTags(List<String> tags);
    Page<Note> searchNotesPaginated(String keyword, int page, int size, String sortBy);
    Page<Note> getNotesByTagPaginated(String tag, int page, int size, String sortBy);
    Page<Note> getNotesByUploaderPaginated(String email, int page, int size, String sortBy);

}
