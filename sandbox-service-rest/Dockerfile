FROM openjdk:17-jdk-alpine
COPY build/libs/sandbox-service-rest-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
