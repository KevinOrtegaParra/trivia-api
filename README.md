# 🧠 Trivia API — Backend

**Trivia API** es una aplicación backend desarrollada en **Spring Boot 3** y **Java 17** que permite a los usuarios registrarse, iniciar sesión (con credenciales o con **Google OAuth2**) y participar en trivias.
Cuenta con autenticación mediante **JWT**, control de roles, y persistencia en **MongoDB Atlas**.

---

## 🚀 Tecnologías principales

| Componente |	Tecnología |
|-------------|-------------|
| Lenguaje	| Java 17 |
| Framework	| Spring Boot 3 |
| Seguridad	| Spring Security + JWT |
| OAuth2	| Google Sign-In |
| Base de datos |	MongoDB Atlas |
| ODM |	Spring Data MongoDB |
| Documentación |	Swagger / OpenAPI |
| Despliegue |	Render (Docker) |

---

## 🗂️ Estructura del proyecto |
```
trivia-api/
├── src/
│   ├── main/java/com/trivia/api/
│   │   ├── auth/                  # Lógica de autenticación, JWT y OAuth2
│   │   ├── controller/            # Controladores REST
│   │   ├── model/                 # Entidades (Documentos Mongo)
│   │   ├── repository/            # Repositorios MongoRepository
│   │   ├── service/               # Lógica de negocio
│   │   └── TriviaApiApplication.java
│   └── main/resources/
│       ├── application.properties
│       └── static/
└── pom.xml
```
---

## ⚙️ Configuración local

### 1️⃣ Requisitos previos
- Java 17  
- Maven 3.9+  
- Cuenta en **MongoDB Atlas**  
- Proyecto configurado en **Google Cloud Console** para OAuth2  
- Postman o Insomnia para probar los endpoints  

### 2️⃣ Configura tu base de datos MongoDB Atlas

En tu archivo application.properties agrega:
```
spring.data.mongodb.uri=${MONGODB_URI}

Si lo usas localmente, puedes poner algo como:

spring.data.mongodb.uri=mongodb+srv://<usuario>:<password>@cluster.mongodb.net/trivia_db
```

---

## 🔑 Variables de entorno

Cuando despliegues en Render, asegúrate de configurar las siguientes variables:

| Variable |	Descripción |
|-------------|-------------|
| MONGODB_URI |	URI de conexión de MongoDB Atlas |
| JWT_SECRET |	Clave secreta para firmar tokens JWT |
| GOOGLE_CLIENT_ID |	ID de cliente de Google Cloud |
| GOOGLE_CLIENT_SECRET |	Secreto del cliente de Google Cloud |
| ALLOWED_ORIGINS	| URL del frontend (por ejemplo, http://localhost:5173) |

---

## 🔐 Autenticación

El sistema soporta:

Login tradicional (correo + contraseña)

Login con Google OAuth2

Cuando un usuario inicia sesión correctamente, el backend devuelve un JWT, que debe enviarse en cada solicitud protegida con:

Authorization: Bearer <token>

---

## 📡 Endpoints principales

🔸 Autenticación
| Método	| Endpoint |	Descripción |
|-------------|-------------|-------------|
| POST |	/api/v1/auth/register |	Registrar usuario manualmente |
| POST | /api/v1/auth/login |	Iniciar sesión con usuario y contraseña |
| POST | /api/v1/auth/google |	Iniciar sesión con Google |

🔸 Usuario
| Método |	Endpoint |	Descripción |
|-------------|-------------|-------------|
| GET |	/api/v1/users/me |	Información del usuario autenticado
| GET	| /api/v1/users |	Listar usuarios (solo ADMIN)

---

## 🧰 Roles

El sistema usa un enum Role para controlar permisos:
```
public enum Role {
    USER,
    ADMIN
}
```

Cada nuevo usuario (normal o Google) se registra con el rol USER.

---

## 🧪 Pruebas locales

Para probar los endpoints, puedes usar Swagger UI o Postman:

Swagger:

http://localhost:8080/api/v1/swagger-ui/index.html#


Postman (ejemplo):

POST http://localhost:8080/api/v1/auth/login
```
{
  "email": "user@mail.com",
  "password": "123456"
}
```
---

## 🐳 Despliegue con Docker y Render

El proyecto usa un Dockerfile multietapa:
```
# Etapa 1: construir el JAR
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: ejecutar la app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

Render compila y ejecuta automáticamente al hacer push en la rama main.

---

## 🧾 Licencia

Este proyecto está bajo la licencia MIT.
Puedes usarlo, modificarlo y compartirlo libremente dando crédito al autor.

---

## 👨‍💻 Autor

Kevin Antonio Ortega Parra
Desarrollador Backend Java
🔗 GitHub: [@KevinOrtegaParra](https://github.com/KevinOrtegaParra)
