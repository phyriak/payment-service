FROM eclipse-temurin:21-jdk

WORKDIR /payment-service

COPY target/*.jar payment-service-2.9-SNAPSHOT.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "payment-service-2.9-SNAPSHOT.jar"]