#first stage: build the application
FROM openjdk:21-jdk-slim AS build
COPY . /app
WORKDIR /app
RUN ./mvnw package -DskipTests

#second stage: create a slim image
FROM eclipse-temurin:21-jre
COPY --from=build /app/target/stockspree-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
