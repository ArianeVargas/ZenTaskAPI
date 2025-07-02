package com.zentaskapi.dto.task;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.zentaskapi.entity.taskmanagerapi.TaskPriority;
import com.zentaskapi.entity.taskmanagerapi.TaskStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    @Size(min = 3, max = 255, message = "El título debe tener entre 3 y 255 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String description;

    @FutureOrPresent(message = "La fecha de vencimiento no puede ser en el pasado")
    private LocalDateTime dueDate;

    private TaskPriority priority;

    private TaskStatus status;

    @Min(value = 0, message = "El tiempo estimado no puede ser negativo")
    private Integer estimatedTime;

    @Min(value = 0, message = "El tiempo dedicado no puede ser negativo")
    private Integer timeSpent; 

    private UUID assignedToId;

    private UUID projectId; 

    private Set<UUID> tagIds; 
}
