FROM eclipse-temurin:17-jdk-alpine
EXPOSE 9060
VOLUME /tmp

ADD target/MINTYN-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
