FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn -Ddatabase.url=jdbc:postgresql://vue-chat-api-build-db:5432/postgres clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENV REACTIVE_POSTGRES_URL=r2dbc:postgresql://localhost:5432/postgres \
	POSTGRES_URL=jdbc:postgresql://localhost:5432/postgres \
	DEBEZIUM_URL=http://localhost:8083/connectors \
	KAFKA_HOST=localhost:9092 \
	KSQLDB_URL=http://localhost:8088/ksql

ENTRYPOINT ["java", "-jar", "app.jar"]
