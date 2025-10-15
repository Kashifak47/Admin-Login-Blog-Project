# ===========================
# Build Stage
# ===========================
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy everything to container
COPY . .

# Build the Spring Boot jar
RUN mvn clean package -DskipTests

# ===========================
# Runtime Stage
# ===========================
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/adminloginproject-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Set memory limits and run jar
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-jar", "app.jar"]
