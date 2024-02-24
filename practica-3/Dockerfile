FROM gradle AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootjar --no-daemon

FROM eclipse-temurin:17.0.8.1_1-jre-alpine
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

ENV PUERTO=8080
EXPOSE ${PUERTO}

VOLUME /logs

ENTRYPOINT ["java", "-jar", "/app/app.jar"]