# Use a base image with JDK 17
FROM openjdk:17-jdk-slim AS builder
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

RUN apt-get update && \
    apt-get install -y git

WORKDIR /app

COPY pom.xml ./
COPY configs/pom.xml configs/
COPY config-server/pom.xml config-server/
COPY entities/pom.xml entities/
COPY productOrders/pom.xml productOrders/
COPY proxyserver/pom.xml proxyserver/
COPY repo/pom.xml repo/
COPY service-registry/pom.xml service-registry/
COPY adminstration/pom.xml adminstration/


COPY configs/src configs/src
COPY config-server/src config-server/src
COPY entities/src entities/src
COPY productOrders/src productOrders/src
COPY proxyserver/src proxyserver/src
COPY repo/src repo/src
COPY service-registry/src service-registry/src
COPY adminstration/src adminstration/src


COPY config-server/src/main/resources config-server/src/main/resources
COPY productOrders/src/main/resources productOrders/src/main/resources
COPY proxyserver/src/main/resources proxyserver/src/main/resources
COPY service-registry/src/main/resources service-registry/src/main/resources
COPY adminstration/src/main/resources adminstration/src/main/resources

RUN mvn install -N
RUN mvn install -pl configs,proxyserver -am


EXPOSE 8086

CMD mvn spring-boot:run -pl proxyserver


