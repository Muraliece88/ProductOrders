#!/bin/sh
java -jar /app/service-registry-1.0-SNAPSHOT.jar & sleep 30
java -jar /app/config-server-1.0-SNAPSHOT.jar
java -jar /app/proxyserver-1.0-SNAPSHOT.jar
java -jar /app/productOrders-1.0-SNAPSHOT.jar && sleep 30
java -jar /app/adminstration-1.0-SNAPSHOT.jar
wait