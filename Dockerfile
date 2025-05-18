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

# Set Spring profile to prod
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "/app/target/meetingroommanager-0.0.1-SNAPSHOT.jar"]
