
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
- Postman

---

## ⚙️ Configuración y Ejecución

### ✅ Requisitos Previos

- JDK 17+
- Maven 3.x
- PostgreSQL 14+

### 🔐 Generar claves para JWT (RS256)

Antes de ejecutar la aplicación, debes generar las llaves pública y privada necesarias:

```bash
openssl genrsa -out private.key 2048
openssl rsa -in private.key -pubout -out public.key
```

Guárdalas en la ruta `src/main/resources/jwt/`.

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
   Authorization: Bearer Token <tu_jwt_token>
   ```

---

## 📦 Endpoints Principales

### 🌐 Públicos

| Método | Endpoint             | Acción   | 
|--------|----------------------|----------|
| GET    | `/ZenTaskAPI`        | Verificar estado del backend   |   
| POST   | `/api/auth/register` | Registro de usuario nuevo      |
| POST   | `/api/auth/login`    | Autenticación (login)          |

---

### 👤 Usuarios

| Método | Endpoint                       | Acción                     | Acceso                                    |
|--------|--------------------------------|----------------------------|-------------------------------------------|
| GET    | `/api/users`                   | Listar todos los usuarios  | ADMIN                                     |
| GET    | `/api/users/me`                | Usuario actual             | el mismo usuario                                   |
| GET    | `/api/users/{username}`              | Ver detalles de un usuario | ADMIN, PROJECT_MANAGER, el mismo usuario  |
| POST   | `/api/users`                   | Crear nuevo usuario        | ADMIN                                     |
| PUT    | `/api/users/{id}`              | Editar usuario existente   | ADMIN, el mismo usuario                   |
| DELETE | `/api/users/{id}`              | Eliminar usuario           | ADMIN                                     |
| GET    | `/api/users/{id}/time-entries` | Ver registros de tiempo    | ADMIN, el mismo usuario, MANAGER          |

---

### ✅ Tareas

| Método | Endpoint                      | Acción                          | Acceso                     |
|--------|-------------------------------|----------------------------------|----------------------------|
| GET    | `/api/tasks`                  | Listar todas las tareas         | Todos los roles            |
| GET    | `/api/tasks/{id}`             | Ver detalles de una tarea       | Participantes del proyecto |
| POST   | `/api/tasks`                  | Crear una nueva tarea           | MANAGER, ADMIN             |
| PUT    | `/api/tasks/{id}`             | Editar tarea                    | Asignado o MANAGER         |
| PATCH  | `/api/tasks/{id}/status`      | Cambiar estado de la tarea      | Asignado                   |
| PATCH  | `/api/tasks/{id}/assign`      | Asignar tarea a un usuario      | MANAGER                    |
| DELETE | `/api/tasks/{id}`             | Eliminar tarea                  | MANAGER, ADMIN             |

---

### ⏱️ Registros de Tiempo

| Método | Endpoint                            | Acción                            | Acceso                 |
|--------|-------------------------------------|-----------------------------------|------------------------|
| POST   | `/api/tasks/{id}/time-entries`      | Crear registro de tiempo          | Asignado               |
| GET    | `/api/users/{userId}/time-entries`  | Ver registros del usuario         | ADMIN, el mismo usuario|

---

### 💬 Comentarios

| Método | Endpoint                       | Acción                        | Acceso        |
|--------|--------------------------------|-------------------------------|---------------|
| POST   | `/api/tasks/{id}/comments`     | Agregar comentario            | Participantes |
| GET    | `/api/tasks/{id}/comments`     | Ver comentarios de la tarea   | Participantes |

---

### 🏷️ Tags

| Método | Endpoint                      | Acción                       | Acceso         |
|--------|-------------------------------|------------------------------|----------------|
| POST   | `/api/tags`                   | Crear nuevo tag              | ADMIN, MANAGER |
| GET    | `/api/tags`                   | Listar todos los tags        | Todos          |
| POST   | `/api/tasks/{id}/tags`        | Asignar tags a una tarea     | MANAGER        |

---

### 📎 Archivos Adjuntos

| Método | Endpoint                              | Acción                          | Acceso       |
|--------|---------------------------------------|----------------------------------|--------------|
| POST   | `/api/tasks/{id}/attachments`         | Subir archivo a una tarea        | Asignado     |
| GET    | `/api/tasks/{id}/attachments`         | Ver archivos de una tarea        | Participantes|

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