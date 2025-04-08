# Build stage
FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Download dependencies and build the application
RUN mvn clean package -DskipTests

# Package stage (runtime)
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
