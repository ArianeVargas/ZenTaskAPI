package com.zentaskapi.dto.task;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.zentaskapi.entity.taskmanagerapi.TaskPriority;
import com.zentaskapi.entity.taskmanagerapi.TaskStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private TaskPriority priority;
    private TaskStatus status;
    private Integer estimatedTime;
    private Integer timeSpent;
    private UUID assignedToId; // Solo el ID del usuario asignado
    private String assignedToUsername; // Opcional: el username del usuario asignado para conveniencia
    private UUID projectId; // Solo el ID del proyecto
    private String projectName; // Opcional: el nombre del proyecto para conveniencia
    private Set<UUID> tagIds; // Solo los IDs de las etiquetas
    private Set<String> tagNames; // Opcional: los nombres de las etiquetas
    private Set<UUID> attachmentIds; // Solo los IDs de los adjuntos
    private Set<UUID> commentIds; // Solo los IDs de los comentarios
    private Set<UUID> timeEntryIds; // Solo los IDs de los registros de tiempo
}