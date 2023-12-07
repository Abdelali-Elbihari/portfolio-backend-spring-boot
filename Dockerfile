# Build Stage
FROM maven:3.8.4-openjdk-17-slim AS builder
COPY . .
RUN mvn clean package  -DskipTests=true

# Final Stage
FROM eclipse-temurin:17-jdk-alpine
COPY --from=builder /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
