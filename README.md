🚀 Zentask API
Una API RESTful para la gestión de tareas y proyectos, desarrollada con Spring Boot.

📝 Descripción
Esta API proporciona funcionalidades completas para la gestión de usuarios, proyectos, tareas, comentarios, tags y archivos adjuntos. Incorpora seguridad robusta con Spring Security y JWT para autenticación y autorización basada en roles (ADMIN, PROJECT_MANAGER, DEVELOPER, USER). Utiliza PostgreSQL como base de datos.

🛠️ Tecnologías Clave
Spring Boot

Spring Security + JWT

Spring Data JPA

PostgreSQL

Springdoc-OpenAPI (para documentación de API)

⚙️ Configuración y Ejecución
Requisitos Previos
JDK 17+

Maven 3.x

PostgreSQL 14+

Pasos
Clonar el repositorio:

git clone https://github.com/ArianeVargas/ZenTaskAPI
cd zentask-api

Configurar la Base de Datos:

Crea una base de datos PostgreSQL llamada zentask.

Configura las siguientes variables de entorno (¡recomendado por seguridad!):

DB_URL (ej: jdbc:postgresql://localhost:5432/zentask)

DB_USERNAME

DB_PASSWORD

(Opcional: valores por defecto en application.properties para desarrollo local.)

Compilar y Ejecutar:

mvn clean install
java -jar target/zentask-api-0.0.1-SNAPSHOT.jar # Ajusta el nombre del JAR

La API se iniciará en http://localhost:8085/ZenTaskAPI.

🌐 Documentación de la API
Accede a la documentación interactiva de Swagger UI una vez que la API esté corriendo:

Swagger UI: http://localhost:8085/ZenTaskAPI/swagger-ui.html

Autenticación
Para acceder a los endpoints protegidos, obtén un JWT del endpoint /api/auth/login e inclúyelo en el encabezado Authorization con el prefijo Bearer (ej: Authorization: Bearer <tu_jwt_token>).

🧪 Pruebas
Ejecuta las pruebas del proyecto con:

mvn test

📄 Licencia
Este proyecto está bajo la Licencia MIT.