# ============================================================
# Stage 1 - Build the Spring Boot application
# ============================================================
FROM eclipse-temurin:26-jdk-alpine AS build

WORKDIR /app

COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

RUN --mount=type=cache,target=/root/.m2 \
    chmod +x mvnw && \
    MAVEN_OPTS="-Xmx512m -Xms256m" \
    ./mvnw dependency:go-offline -B \
    -Dmaven.wagon.http.retryHandler.count=5

COPY src src

RUN --mount=type=cache,target=/root/.m2 \
    MAVEN_OPTS="-Xmx512m -Xms256m" \
    ./mvnw clean package -DskipTests \
    -Dmaven.wagon.http.retryHandler.count=5

# ============================================================
# Stage 2 - Runtime image (no build tools)
# ============================================================
FROM eclipse-temurin:26-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/target/*.jar app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]