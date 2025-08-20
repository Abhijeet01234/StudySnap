package com.studysnap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String fileName; // Store actual filename saved on server

    private String uploadedBy; // Email or name of the uploader

    private LocalDateTime uploadedAt;
    
    @Column(nullable = false)
    private String status = "PENDING";
    
 // ✅ New field for tags
    @ElementCollection
    @CollectionTable(name = "note_tags", joinColumns = @JoinColumn(name = "note_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    public Note() {}

    public Note(Long id, String title, String description, String fileName,
            String uploadedBy, LocalDateTime uploadedAt, String status, List<String> tags) {
    super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.fileName = fileName;
		this.uploadedBy = uploadedBy;
		this.uploadedAt = uploadedAt;
		this.status = status;
		this.tags = tags;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<String> getTags() {
		return tags;
	}
    public void setTags(List<String> tags) {
    	this.tags = tags;
    }

	@Override
	public String toString() {
		return "Note [id=" + id + 
				", title=" + title + 
				", description=" + description + 
				", fileName=" + fileName + 
				", uploadedBy=" + uploadedBy + 
				", uploadedAt=" + uploadedAt + 
				", status=" + status +
				", tags=" + tags + "]";
	}
    
//    @Override
//    public String toString() {
//        return "Note{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", fileName='" + fileName + '\'' +
//                ", uploadedBy='" + uploadedBy + '\'' +
//                ", uploadedAt=" + uploadedAt +
//                '}';
//    }
    
}
