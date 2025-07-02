package com.zentaskapi.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.zentaskapi.dto.project.CreateProjectRequest;
import com.zentaskapi.dto.project.ProjectResponse;
import com.zentaskapi.dto.project.UpdateProjectRequest;
import com.zentaskapi.entity.Project;
import com.zentaskapi.entity.ProjectMember;
import com.zentaskapi.entity.Task;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "taskIds", expression = "java(mapTaskIds(project.getTasks()))")
    @Mapping(target = "memberIds", expression = "java(mapMemberIds(project.getMembers()))")
    ProjectResponse toDto(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "members", ignore = true)
    Project fromCreateRequest(CreateProjectRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "members", ignore = true)
    void updateFromDto(UpdateProjectRequest request, @MappingTarget Project project);

    default Set<UUID> mapTaskIds(Set<Task> tasks) {
        return tasks == null ? null : tasks.stream().map(Task::getId).collect(Collectors.toSet());
    }

    default Set<UUID> mapMemberIds(Set<ProjectMember> members) {
        return members == null ? null : members.stream().map(pm -> pm.getUser().getId()).collect(Collectors.toSet());
    }
}
