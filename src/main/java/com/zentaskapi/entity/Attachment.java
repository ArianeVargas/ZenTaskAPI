package com.zentaskapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "file_url", nullable = false, columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;

    //Un archivo adjunto pertenece a una tarea
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false, referencedColumnName = "id")
    private Task task;

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }
}
