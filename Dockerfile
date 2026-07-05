FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:26-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Pobranie JMX Exportera
RUN apt-get update && \
    apt-get install -y curl && \
    mkdir -p /opt/jmx && \
    curl -fSL \
      https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/1.0.1/jmx_prometheus_javaagent-1.0.1.jar \
      -o /opt/jmx/jmx_prometheus_javaagent.jar && \
    ls -lh /opt/jmx

COPY monitoring/jmx/config.yml /opt/jmx/config.yml

EXPOSE 8082
EXPOSE 9404

ENTRYPOINT ["java", "-javaagent:/opt/jmx/jmx_prometheus_javaagent.jar=9404:/opt/jmx/config.yml", "-jar", "app.jar"]