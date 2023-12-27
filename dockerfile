FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar users.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8090