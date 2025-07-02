package com.zentaskapi.dto.project;

import java.time.LocalDateTime;

import com.zentaskapi.entity.taskmanagerapi.ProjectStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProjectRequest {

    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    private String name;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String description;

    @FutureOrPresent(message = "La fecha de inicio debe ser presente o futura")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private ProjectStatus status;
}
