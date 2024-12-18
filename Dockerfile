FROM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS run

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8085

CMD ["java", "-jar", "app.jar"]
