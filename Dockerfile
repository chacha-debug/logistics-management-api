# ============================================================
# Stage 1 — Build the Spring Boot application
# ============================================================
FROM eclipse-temurin:26-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and pom first so dependencies get cached
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Ensure the wrapper is executable and download dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy the actual source and build the jar (skip tests in the image build)
COPY src src
RUN ./mvnw clean package -DskipTests

# ============================================================
# Stage 2 — Runtime image (small, no build tools)
# ============================================================
FROM eclipse-temurin:26-jre-alpine

WORKDIR /app

# Create a non-root user to run the app
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Switch to non-root user
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]