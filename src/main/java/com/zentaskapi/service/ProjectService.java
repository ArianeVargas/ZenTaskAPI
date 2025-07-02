package com.zentaskapi.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.zentaskapi.dto.project.CreateProjectRequest;
import com.zentaskapi.dto.project.ProjectResponse;
import com.zentaskapi.entity.Project;
import com.zentaskapi.exception.ResourceNotFoundException;
import com.zentaskapi.mapper.ProjectMapper;
import com.zentaskapi.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll()
            .stream()
            .map(projectMapper::toDto)
            .toList();
    }

    public ProjectResponse findByProject(UUID id) {
        return projectRepository.findById(id)
            .map(projectMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id" + id));
    }

    public ProjectResponse createProject(CreateProjectRequest req) {
        Project project = projectMapper.fromCreateRequest(req);
        return projectMapper.toDto(projectRepository.save(project));
    }
}
