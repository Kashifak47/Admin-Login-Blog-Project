FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the jar directly
COPY target/yourapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
