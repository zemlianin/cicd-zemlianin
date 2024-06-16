FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY converter/pom.xml converter/
COPY converter/src converter/src
WORKDIR /app/converter
RUN mvn clean install
RUN mvn -o compile

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/converter/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
