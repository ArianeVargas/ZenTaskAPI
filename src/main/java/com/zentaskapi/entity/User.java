package com.zentaskapi.entity;

import com.zentaskapi.entity.taskmanagerapi.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    // Relación correcta: un usuario puede tener muchas tareas asignadas
    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> assignedTasks = new HashSet<>();

    // Un usuario puede escribir muchos comentarios
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    // Un usuario puede registrar muchos tiempos
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TimeEntry> timeEntries = new HashSet<>();

    // un usuario puede se un miembro de muchos proyectos
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectMember> projectMemberships = new HashSet<>();
}
