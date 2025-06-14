FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/AuthService-0.0.1-SNAPSHOT.jar AuthService-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "AuthService-0.0.1-SNAPSHOT.jar"]

