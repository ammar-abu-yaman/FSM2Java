FROM openjdk:19
COPY target/server-0.0.1-SNAPSHOT.jar server.jar
CMD ["java", "-jar", "server.jar"]
