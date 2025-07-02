package com.zentaskapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.zentaskapi.dto.task.CreateTaskRequest;
import com.zentaskapi.dto.task.TaskResponse;
import com.zentaskapi.entity.Project;
import com.zentaskapi.entity.Tag;
import com.zentaskapi.entity.Task;
import com.zentaskapi.entity.User;
import com.zentaskapi.mapper.TaskMapper;
import com.zentaskapi.repository.ProjectRepository;
import com.zentaskapi.repository.TagRepository;
import com.zentaskapi.repository.TaskRepository;
import com.zentaskapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TaskMapper taskMapper;

    public List<TaskResponse> findAll() {
        return taskRepository.findAll()
            .stream()
            .map(taskMapper::toDto)
            .toList();
    }

    public TaskResponse createTask(CreateTaskRequest req) {
        Task task = taskMapper.fromCreateRequest(req);

        Project project = projectRepository.findById(req.getProjectId())
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        task.setProject(project);

        if (req.getAssignedToId() != null) {
            User user = userRepository.findById(req.getAssignedToId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            task.setAssignedTo(user);
        }

        if (req.getTagIds() != null && !req.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(req.getTagIds()));
            task.setTags(tags);
        }

        return taskMapper.toDto(taskRepository.save(task));
    }

}
