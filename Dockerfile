FROM adoptopenjdk:11-jre-hotspot

ARG JAR_FILE=*.jar
COPY /target/joke-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "application.jar"]