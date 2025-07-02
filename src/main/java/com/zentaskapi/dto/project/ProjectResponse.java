package com.zentaskapi.dto.project;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.zentaskapi.entity.taskmanagerapi.ProjectStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponse {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
    private Set<UUID> taskIds;    
    private Set<UUID> memberIds;   
}
