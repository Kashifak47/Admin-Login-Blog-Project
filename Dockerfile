# Use official Java 17 JDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the pre-built jar from target folder
COPY target/yourapp-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (Render assigns dynamic port)
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "app.jar"]
