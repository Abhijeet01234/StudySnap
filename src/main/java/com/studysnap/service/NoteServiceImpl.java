package com.studysnap.service;

import com.studysnap.model.Note;
import com.studysnap.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public List<Note> getNotesByUploader(String email) {
        return noteRepository.findByUploadedBy(email);
    }

    @Override
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    public List<Note> getNotesByStatus(String status) {
        return noteRepository.findByStatus(status);
    }

    @Override
    public List<Note> getNotesByUploaderAndStatus(String uploadedBy, String status) {
        return noteRepository.findByUploadedByAndStatus(uploadedBy, status);
    }

    @Override
    public String updateNoteStatus(Long id, String status) {
        Note note = noteRepository.findById(id).orElse(null);
        if (note == null) {
            return "Note not found";
        }
        note.setStatus(status);
        noteRepository.save(note);
        return "Note status updated to: " + status;
    }

    @Override
    public List<Note> searchNotes(String keyword) {
        return noteRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public List<Note> getNotesByTag(String tag) {
        return noteRepository.findByTagsContainingIgnoreCase(tag);
    }

    @Override
    public List<String> getAllTags() {
        return noteRepository.findAllDistinctTags();
    }

    @Override
    public List<Note> getNotesByMultipleTags(List<String> tags) {
        List<String> lowerTags = tags.stream()
                                     .map(String::toLowerCase)
                                     .collect(Collectors.toList());
        return noteRepository.findByTagsInIgnoreCase(lowerTags);
    }

    @Override
    public Page<Note> searchNotesPaginated(String keyword, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return noteRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Override
    public Page<Note> getNotesByTagPaginated(String tag, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return noteRepository.findByTagsContainingIgnoreCase(tag, pageable);
    }
    @Override
    public Page<Note> getNotesByUploaderPaginated(String email, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return noteRepository.findByUploadedBy(email, pageable);
    }

}
