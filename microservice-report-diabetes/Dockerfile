FROM openjdk:8
LABEL maintainer="Christine@email.fr"
EXPOSE 8083
ADD target/microservice-report-diabetes.jar microservice-report-diabetes.jar
ENTRYPOINT ["java","-jar","microservice-report-diabetes.jar"]