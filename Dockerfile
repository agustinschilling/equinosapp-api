#FROM openjdk:17-alpine
#
#RUN mkdir -p /opt/equinosapp-api/images
#
#COPY target/equinosapp-api-0.0.1-SNAPSHOT.jar /app/equinosapp-api-0.0.1-SNAPSHOT.jar
#
#ENTRYPOINT ["java", "-jar", "/app/equinosapp-api-0.0.1-SNAPSHOT.jar"]

#### TO DEPLOY ON FLY.io
# Etapa 1: Construcción
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia los archivos de configuración y dependencias
COPY pom.xml .
COPY src ./src

# Construye el proyecto y genera el .jar
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM openjdk:17-alpine

WORKDIR /app

# Copia el .jar desde la etapa de construcción
COPY --from=build /app/target/equinosapp-api-0.0.1-SNAPSHOT.jar .

# Expone el puerto en el que tu aplicación se ejecuta (por ejemplo, 8080)
EXPOSE 8080

# Comando de entrada
ENTRYPOINT ["java", "-jar", "equinosapp-api-0.0.1-SNAPSHOT.jar"]