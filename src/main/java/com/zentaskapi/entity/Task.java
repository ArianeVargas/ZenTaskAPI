package com.zentaskapi.entity;

import com.zentaskapi.entity.taskmanagerapi.TaskPriority;
import com.zentaskapi.entity.taskmanagerapi.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private TaskStatus status;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "time_spent")
    private Integer timeSpent = 0;

    //Una tarea pertenece a un usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private User assignedTo;

    // Una tarea pertenece a un proyecto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    //Una tarea puede tener muchas etiquetas y una etiqueta puede tener muchas tareas
    @ManyToMany
    @JoinTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    //Una tarea puede tener muchos archivos adjuntos
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attachment> attachments;

    //Una tarea puede tener muchos comentarios
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    //Una tarea puede tener muchos registros de tiempo
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TimeEntry> timeEntries;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
