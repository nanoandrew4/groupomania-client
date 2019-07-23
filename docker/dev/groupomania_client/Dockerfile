FROM openjdk:8-jdk-alpine

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

RUN addgroup -S groupomania-client-admin && adduser -S groupomania-client-admin -G groupomania-client-admin

USER groupomania-client-admin
ENTRYPOINT ["java","-jar","/app.jar"]