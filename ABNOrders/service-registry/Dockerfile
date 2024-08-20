# Use a base image with JDK 17
FROM openjdk:17-jdk-slim AS DEPS
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

RUN apt-get update && \
    apt-get install -y git

WORKDIR /app


COPY configs/pom.xml configs/
COPY config-server/pom.xml config-server/
COPY entities/pom.xml entities/
COPY productOrders/pom.xml productOrders/
COPY proxyserver/pom.xml proxyserver/
COPY repo/pom.xml repo/
COPY service-registry/pom.xml service-registry/
COPY adminstration/pom.xml adminstration/
COPY pom.xml ./
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=repo,entities,productOrders,proxyserver,adminstration,config-server

FROM openjdk:17-jdk-slim AS BUILDER
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

RUN apt-get update && \
    apt-get install -y git
WORKDIR /app

COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /app/ /app
COPY configs/src configs/src
COPY service-registry/src service-registry/src


EXPOSE 8761
WORKDIR /app/service-registry
CMD mvn spring-boot:run


