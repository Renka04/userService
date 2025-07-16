# Use official OpenJDK 21 Alpine image for small size
FROM openjdk:21-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the jar file built by Maven into the container
COPY target/user-0.0.1-SNAPSHOT.jar user.jar

# Expose the port your Spring Boot app runs on (default 8080)
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "user.jar"]
