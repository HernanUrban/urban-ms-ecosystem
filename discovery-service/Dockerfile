FROM openjdk:8-jdk-alpine
RUN apk --update add curl
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
HEALTHCHECK --interval=10s --timeout=10s --retries=30\
  CMD curl -f http://localhost:8761/health || exit 1