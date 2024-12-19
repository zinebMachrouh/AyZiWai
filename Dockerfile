FROM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS run

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8085

CMD ["java", "-jar", "app.jar"]
=======
# Use OpenJDK 21 as the base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the project files
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src ./src/

# Make the mvnw script executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw package -DskipTests

# Run the application
CMD ["java", "-jar", "target/AyZiWai-0.0.1-SNAPSHOT.jar"]
