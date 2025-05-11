# Use the official OpenJDK image as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the Maven pom.xml file
COPY pom.xml .

# Copy the Maven wrapper files
COPY mvnw .
COPY .mvn .mvn

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Create a volume for persistent data
VOLUME /tmp

# Set environment variables for PostgreSQL connection
# These will be overridden by external environment variables when running the container
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/meeting_room_manager
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=Password@0@

# Run the application
ENTRYPOINT ["java", "-jar", "/app/target/meetingroommanager-0.0.1-SNAPSHOT.jar"]
