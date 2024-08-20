# PRODUCT SEARCH AND PLACEMENT API

## Frameworks Used

Java 17, Spring Boot 3, Maven 3.8.
For removing bolier plate code --- Lombok.
API Documentation - OpenApi 3.0.3.
Database- mysql
Container- Docker
Testing - Junit jupiter, Mockito, MockMvc Embedded DB - H2.
No checkstyles files used

## Code setup and run
1. Clone the project from gitHub
2. Navigate to project root folder and execute docker-compose up --build, in this stage only database will be running
3. Navigate to service-registry child modules and mvn clean install spring-boot:run 
4. Navigate to config-server child modules and mvn clean install spring-boot:run
5. Navigate to proxyserver child modules and mvn clean install spring-boot:run
6. Navigate to productOrders child modules and mvn clean install spring-boot:run
7. Navigate to adminstration child modules and mvn clean install spring-boot:run


## Packaging structure

Considering microservice architecture functional packaging is considered and common components are treated to as modules
to avoid changes at multiple location

## Production ready considerations:

Spring boot application runs with active profiles hence properties per environment is placed and the prod profile is configured to point the H2 database. In the real time scenario, different databases can be used based on the environment.


1. Spring security is enabled with different credentials for prod with different roles for differnt endpoint. Roles can also be configured in tables and can be made run time rather than hardcoding
2. Actuator endpoints are made available to capture health and metrics which is accessible for user with specific role 
3. Passwords are hardcoded in the code or property file as there is no vault integration done and in real time it can be fetched from secure vaults
4. The application related configuration files are located in different git hub repo (mentioned in one of the application properties)
