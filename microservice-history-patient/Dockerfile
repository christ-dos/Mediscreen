FROM openjdk:8-jdk-alpine
LABEL maintainer="Christine@email.fr"
EXPOSE 8082
ADD target/microservice-history-patient-mongoBD.jar microservice-history-patient-mongoBD.jar
ENTRYPOINT ["java","-jar","microservice-history-patient-mongoBD.jar"]