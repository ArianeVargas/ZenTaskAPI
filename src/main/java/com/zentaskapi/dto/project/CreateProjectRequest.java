package com.zentaskapi.dto.project;

import java.time.LocalDateTime;

import com.zentaskapi.entity.taskmanagerapi.ProjectStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProjectRequest {

    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    private String name;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String description;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio debe ser presente o futura")
    private LocalDateTime startDate;

    @NotNull(message = "La fecha de finalización es obligatoria")
    private LocalDateTime endDate;

    @NotNull(message = "El estado del proyecto es obligatorio")
    private ProjectStatus status;
}
