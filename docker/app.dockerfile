FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /opt/app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY ./src ./src

RUN mvn -B -e clean package -DskipTests
ARG BBB=1
FROM eclipse-temurin:21-jre-alpine

ARG APP_NAME
ARG APP_VERSION

RUN addgroup -g 1000 app_initiator &&  \
    adduser -u 1000 -G app_initiator -s /sbin/nologin -D app_initiator

COPY --from=builder /opt/app/target/$APP_NAME-$APP_VERSION.jar  /app/

RUN chown -R app_initiator:app_initiator /app
USER app_initiator

EXPOSE 8080
ENTRYPOINT ["sh","-c","exec java -jar /app/$APP_NAME-$APP_VERSION.jar"]