package com.studysnap.repository;

import com.studysnap.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUploadedBy(String uploadedBy);

    List<Note> findByStatus(String status);

    List<Note> findByUploadedByAndStatus(String uploadedBy, String status);

    List<Note> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword, String descriptionKeyword);

    // ✅ Single tag
    List<Note> findByTagsContainingIgnoreCase(String tag);

    // ✅ Paginated search
    Page<Note> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword,
            Pageable pageable
    );

    // ✅ Paginated tag
    Page<Note> findByTagsContainingIgnoreCase(String tag, Pageable pageable);
    Page<Note> findByUploadedBy(String uploadedBy, Pageable pageable);

    // ✅ All distinct tags (requires @ElementCollection in Note)
    @Query("SELECT DISTINCT t FROM Note n JOIN n.tags t")
    List<String> findAllDistinctTags();

    // ✅ Multiple tags (case-insensitive)
    @Query("SELECT DISTINCT n FROM Note n JOIN n.tags t WHERE LOWER(t) IN :tags")
    List<Note> findByTagsInIgnoreCase(@Param("tags") List<String> tags);
}
