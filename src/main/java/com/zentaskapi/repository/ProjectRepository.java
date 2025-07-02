package com.zentaskapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zentaskapi.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID>{

}
