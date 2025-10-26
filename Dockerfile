FROM openjdk:17

WORKDIR /app

COPY ./target/trivia-api-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8084

CMD ["java","-Dspring.profiles.active=docker","-jar", "app.jar"]