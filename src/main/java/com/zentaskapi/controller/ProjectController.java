package com.zentaskapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentaskapi.dto.project.CreateProjectRequest;
import com.zentaskapi.dto.project.ProjectResponse;
import com.zentaskapi.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity <List<ProjectResponse>> getAllProject() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid CreateProjectRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

}
