FROM openjdk:17-alpine

RUN mkdir -p /opt/equinosapp-api/images

COPY target/equinosapp-api-0.0.1-SNAPSHOT.jar /app/equinosapp-api-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/equinosapp-api-0.0.1-SNAPSHOT.jar"]
