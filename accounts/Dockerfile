FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY accounts/pom.xml accounts/
COPY accounts/src accounts/src
COPY grpc-models/pom.xml grpc-models/
COPY grpc-models/src grpc-models/src
COPY converter/pom.xml converter/
COPY converter/src converter/src

# WORKDIR /app/grpc-models
RUN mvn clean install -DskipTests

WORKDIR /app/accounts
RUN mvn -o compile

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/accounts/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
