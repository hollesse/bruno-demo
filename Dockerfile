FROM maven:3.9.9-eclipse-temurin-22-alpine@sha256:933407864e22b16e8a79035ee8aea007e2faa22baa603b36b5ed8c664ea4dba3 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline dependency:resolve-plugins
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk-alpine@sha256:4bbdd7904c4ef2ef7ce8667faedca80979af6832bd48d423eb00ba2f87dcf5e4
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]