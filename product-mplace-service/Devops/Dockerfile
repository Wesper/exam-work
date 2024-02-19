FROM openjdk:17.0.1-jdk-oracle
WORKDIR /usr/src/myapp
COPY target/product-mplace-service-0.0.1-SNAPSHOT.jar /usr/src/myapp
ENTRYPOINT ["java", "-jar", "/usr/src/myapp/product-mplace-service-0.0.1-SNAPSHOT.jar"]