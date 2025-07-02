package com.zentaskapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zentaskapi.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, UUID>{

}
