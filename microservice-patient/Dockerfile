FROM openjdk:8
LABEL maintainer="Christine@email.fr"
EXPOSE 8081
ADD target/microservice-patient-mysql.jar microservice-patient-mysql.jar
ENTRYPOINT ["java","-jar","microservice-patient-mysql.jar"]