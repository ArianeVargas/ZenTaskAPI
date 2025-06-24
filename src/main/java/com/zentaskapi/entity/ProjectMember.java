package com.zentaskapi.entity;

import com.zentaskapi.entity.taskmanagerapi.ProjectRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_members")
@IdClass(ProjectMemberId.class) // <- Clave compuesta
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

    // Clave compuesta (parte 1): Un ProjectMember pertenece a un proyecto
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    // Clave compuesta (parte 2): Un ProjectMember también pertenece a un usuario
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Rol que tiene este usuario dentro del proyecto (ej: MANAGER, DEVELOPER, TESTER)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private ProjectRole role;
}
