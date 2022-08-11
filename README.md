# Testing a Spring Boot Rabbit MQ Application with Testcontainers.

This example project is derived from the article https://gaddings.io/testing-spring-boot-apps-with-rabbitmq-using-testcontainers/

The examples have been modified to use the latest versions of Spring Boot (2.7.2) and Testcontainers (1.17.3) https://www.testcontainers.org.
The tests have been migrated to JUnit 5 with `@SpyBean` and Mockito verify as well as
the specialized test container `RabbitMQContainer` instead of a generic container. 

Run the project with `./mvnw clean verify -Pintegration-test`