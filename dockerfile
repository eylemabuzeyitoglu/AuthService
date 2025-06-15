FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/AuthService-0.0.1-SNAPSHOT.jar /app/AuthService-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/AuthService-0.0.1-SNAPSHOT.jar"]

