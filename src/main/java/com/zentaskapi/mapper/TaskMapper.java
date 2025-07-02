package com.zentaskapi.mapper;

import com.zentaskapi.dto.task.CreateTaskRequest;
import com.zentaskapi.dto.task.TaskResponse;
import com.zentaskapi.dto.task.UpdateTaskRequest;
import com.zentaskapi.entity.*;
import org.mapstruct.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    
    @Mapping(source = "assignedTo.id", target = "assignedToId")
    @Mapping(source = "assignedTo.username", target = "assignedToUsername")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(target = "tagIds", expression = "java(mapTagIds(task.getTags()))")
    @Mapping(target = "tagNames", expression = "java(mapTagNames(task.getTags()))")
    @Mapping(target = "attachmentIds", expression = "java(mapAttachmentIds(task.getAttachments()))")
    @Mapping(target = "commentIds", expression = "java(mapCommentIds(task.getComments()))")
    @Mapping(target = "timeEntryIds", expression = "java(mapTimeEntryIds(task.getTimeEntries()))")
    TaskResponse toDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "timeSpent", constant = "0")
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "timeEntries", ignore = true)
    Task fromCreateRequest(CreateTaskRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "timeEntries", ignore = true)
    void updateFromDto(UpdateTaskRequest request, @MappingTarget Task task);

    default Set<UUID> mapTagIds(Set<Tag> tags) {
        return tags == null ? null : tags.stream().map(Tag::getId).collect(Collectors.toSet());
    }

    default Set<String> mapTagNames(Set<Tag> tags) {
        return tags == null ? null : tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }

    default Set<UUID> mapAttachmentIds(Set<Attachment> attachments) {
        return attachments == null ? null : attachments.stream().map(Attachment::getId).collect(Collectors.toSet());
    }

    default Set<UUID> mapCommentIds(Set<Comment> comments) {
        return comments == null ? null : comments.stream().map(Comment::getId).collect(Collectors.toSet());
    }

    default Set<UUID> mapTimeEntryIds(Set<TimeEntry> timeEntries) {
        return timeEntries == null ? null : timeEntries.stream().map(TimeEntry::getId).collect(Collectors.toSet());
    }
}
