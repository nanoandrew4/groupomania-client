FROM openjdk:8-jdk-alpine

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

RUN addgroup -S groupomania-client-admin && adduser -S groupomania-client-admin -G groupomania-client-admin

USER groupomania-client-admin
ENV JAVA_OPTS="-Dgroupomania.server.backend.url=http://groupomania:8444/api"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]