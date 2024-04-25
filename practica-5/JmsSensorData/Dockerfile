# Compilacion
FROM gradle AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
#RUN chmod +x ./gradlew && ./gradlew build --no-daemon
RUN gradle build --no-daemon

# Ejecucion
FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
