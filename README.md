# 🚀 Zentask API

Una API RESTful para la gestión de tareas y proyectos, desarrollada con **Spring Boot**.

---

## 📝 Descripción

Esta API proporciona funcionalidades completas para la gestión de:

- Usuarios
- Proyectos
- Tareas
- Comentarios
- Etiquetas (tags)
- Archivos adjuntos

Cuenta con seguridad robusta mediante **Spring Security + JWT**, y **autenticación/autorización basada en roles** (`ADMIN`, `PROJECT_MANAGER`, `DEVELOPER`, `USER`). Utiliza **PostgreSQL** como sistema de base de datos relacional.

---

## 🛠️ Tecnologías Clave

- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Springdoc OpenAPI (Swagger UI)

---

## ⚙️ Configuración y Ejecución

### ✅ Requisitos Previos

- JDK 17+
- Maven 3.x
- PostgreSQL 14+

### 🔧 Pasos

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/ArianeVargas/ZenTaskAPI
   cd zentask-api
   ```

2. **Configurar la Base de Datos**

    - Crea una base de datos llamada `zentask` en PostgreSQL.
    - Configura las siguientes variables de entorno:

      ```env
      DB_URL=jdbc:postgresql://localhost:5432/zentask
      DB_USERNAME=tu_usuario
      DB_PASSWORD=tu_contraseña
      ```

   > 💡 También puedes usar `application.properties` para valores locales por defecto.

3. **Compilar y Ejecutar**

   ```bash
   mvn clean install
   java -jar target/zentask-api-0.0.1-SNAPSHOT.jar
   ```

   La API estará disponible en:  
   [http://localhost:8085/ZenTaskAPI](http://localhost:8085/ZenTaskAPI)

---

## 🌐 Documentación de la API

La documentación está disponible vía **Postman** o puedes habilitar **Swagger UI** en desarrollo (`springdoc-openapi`).

### 🔐 Autenticación

Para acceder a los endpoints protegidos:

1. Realiza un `POST` a `/api/auth/login` con credenciales válidas.
2. Obtén el token JWT devuelto.
3. Incluye el token en las peticiones usando el encabezado:

   ```http
   Authorization: Bearer <tu_jwt_token>
   ```

---

## 📦 Endpoints Principales

### 👤 Usuarios

| Método | Endpoint                         | Acceso             |
|--------|----------------------------------|---------------------|
| GET    | `/api/users`                     | ADMIN              |
| GET    | `/api/users/{id}`                | ADMIN, PROJECT_MANAGER, el mismo usuario |
| POST   | `/api/users`                     | ADMIN              |
| PUT    | `/api/users/{id}`                | ADMIN, el mismo usuario |
| DELETE | `/api/users/{id}`                | ADMIN              |
| GET    | `/api/users/{id}/time-entries`   | ADMIN, el mismo usuario, MANAGER |

### ✅ Tareas

| Método | Endpoint                          | Acceso                     |
|--------|-----------------------------------|----------------------------|
| GET    | `/api/tasks`                      | Todos los roles           |
| GET    | `/api/tasks/{id}`                 | Participantes del proyecto |
| POST   | `/api/tasks`                      | MANAGER, ADMIN             |
| PUT    | `/api/tasks/{id}`                 | Asignado o MANAGER         |
| PATCH  | `/api/tasks/{id}/status`          | Asignado                   |
| PATCH  | `/api/tasks/{id}/assign`          | MANAGER                    |
| DELETE | `/api/tasks/{id}`                 | MANAGER, ADMIN             |

### ⏱️ Registros de Tiempo

| Método | Endpoint                                | Acceso           |
|--------|-----------------------------------------|------------------|
| POST   | `/api/tasks/{id}/time-entries`          | Asignado         |
| GET    | `/api/users/{userId}/time-entries`      | ADMIN, el mismo usuario |

### 💬 Comentarios

| Método | Endpoint                         | Acceso           |
|--------|----------------------------------|------------------|
| POST   | `/api/tasks/{id}/comments`       | Participantes    |
| GET    | `/api/tasks/{id}/comments`       | Participantes    |

### 🏷️ Tags

| Método | Endpoint                        | Acceso         |
|--------|---------------------------------|----------------|
| POST   | `/api/tags`                     | ADMIN, MANAGER |
| GET    | `/api/tags`                     | Todos          |
| POST   | `/api/tasks/{id}/tags`          | MANAGER        |

### 📎 Archivos Adjuntos

| Método | Endpoint                            | Acceso       |
|--------|-------------------------------------|--------------|
| POST   | `/api/tasks/{id}/attachments`       | Asignado     |
| GET    | `/api/tasks/{id}/attachments`       | Participantes |

---

## 🧪 Pruebas

Ejecuta las pruebas del proyecto con:

```bash
mvn test
```

También puedes probar la API con herramientas como:

- Postman
- Insomnia
- Swagger UI (si está habilitado)

---

## 📄 Licencia

Este proyecto está bajo la **Licencia MIT**.