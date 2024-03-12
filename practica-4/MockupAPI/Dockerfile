# Etapa 1: Construir la aplicación
FROM gradle AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

# Etapa 2: Crear una imagen mínima para la ejecución
FROM eclipse-temurin:17.0.8.1_1-jre-alpine
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

ENV PUERTO=8080
EXPOSE ${PUERTO}

ENTRYPOINT ["java", "-jar", "/app/app.jar"]