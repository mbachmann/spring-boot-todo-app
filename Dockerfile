#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -DskipTests -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:21-jdk-slim
COPY --from=build /home/app/target/todo-*.jar /usr/local/lib/app.jar
ARG JVM_OPTS
ENV JVM_OPTS=${JVM_OPTS}
# COPY chatapp.keystore.jks /
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
