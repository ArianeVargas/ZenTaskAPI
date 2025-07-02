package com.zentaskapi.dto.task;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.zentaskapi.entity.taskmanagerapi.TaskPriority;
import com.zentaskapi.entity.taskmanagerapi.TaskStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, max = 255, message = "El título debe tener entre 3 y 255 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String description;

    @FutureOrPresent(message = "La fecha de vencimiento no puede ser en el pasado")
    private LocalDateTime dueDate;

    @NotNull(message = "La prioridad no puede ser nula")
    private TaskPriority priority; 

    @NotNull(message = "El estado no puede ser nulo")
    private TaskStatus status; 

    @Min(value = 0, message = "El tiempo estimado no puede ser negativo")
    private Integer estimatedTime;

    private UUID assignedToId; 

    @NotNull(message = "El ID del proyecto es obligatorio")
    private UUID projectId;

    private Set<UUID> tagIds; 
}
