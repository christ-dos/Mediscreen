FROM openjdk:8
LABEL maintainer="Christine@email.fr"
EXPOSE 8080
ADD target/microservice-client-ui.jar microservice-client-ui.jar
ENTRYPOINT ["java","-jar","microservice-client-ui.jar"]