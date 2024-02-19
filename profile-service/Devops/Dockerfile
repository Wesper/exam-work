FROM openjdk:17.0.1-jdk-oracle
WORKDIR /usr/src/myapp
COPY target/profile-service-0.0.1-SNAPSHOT.jar /usr/src/myapp
ENTRYPOINT ["java", "-jar", "/usr/src/myapp/profile-service-0.0.1-SNAPSHOT.jar"]