package com.zentaskapi.entity.taskmanagerapi;

public enum ProjectRole {
    MANAGER,   // controla el proyecto: asignar tareas, modificar
    DEVELOPER, // desarrolla tareas asignadas
    TESTER,    // prueba tareas
    DESIGNER   // diseña tareas
}
