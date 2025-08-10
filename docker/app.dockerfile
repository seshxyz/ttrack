FROM maven:3.9-eclipse-temurin-21 as builder

WORKDIR /opt/app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean install

ARG JAR_APP=target/2track-1.0.0.jar
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -g 1000 app_initiator &&  \
    adduser -u 1000 -G app_initiator -s /sbin/nologin -D app_initiator

COPY --from=builder /opt/app/${JAR_APP} /app.jar

RUN chown app_initiator:app_initiator /app.jar

USER app_initiator

EXPOSE 8080
ENTRYPOINT ["java","-jar","/2track-1.0.0.jar"]