FROM openjdk:17.0.1-jdk-oracle
WORKDIR /usr/src/myapp
COPY target/ui-0.0.1-SNAPSHOT.jar /usr/src/myapp
ENTRYPOINT ["java", "-jar", "/usr/src/myapp/ui-0.0.1-SNAPSHOT.jar"]