package com.zentaskapi.entity.taskmanagerapi;

public enum UserRole {
    ADMIN,     // acceso completo
    MANAGER,   // puede gestionar proyectos/tareas de su equipo
    USER       // solo puede trabajar con lo asignado
}
