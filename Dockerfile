# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/bffservice-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Optional: expose gRPC port if needed
#EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]