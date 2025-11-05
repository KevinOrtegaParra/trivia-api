# Etapa 1: Construir el proyecto con Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos de configuración y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la app ya compilada
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiar el JAR generado en la etapa anterior
COPY --from=build /app/target/trivia-api-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8084

CMD ["java","-Dspring.profiles.active=docker","-jar","app.jar"]