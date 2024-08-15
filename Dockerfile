FROM eclipse-temurin:21.0.4_7-jdk-alpine AS builder
COPY . .
RUN ./mvnw clean package

FROM eclipse-temurin:21.0.4_7-jre-alpine
COPY --from=builder target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]