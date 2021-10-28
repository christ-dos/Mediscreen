# **Mediscreen**

Application that permit assess the potential risk of developing diabetes.
it is an application implemented using a microservices architecture.
composed of 4 microservices:

-microservice-patient

-microservice-history-patient

-microservice-report-diabetes

-microservice-client-Ui

#### **Prerequisites What things you need to install the software and how to install them**

Java 1.8 Maven 3.6.2 Mysql 8.0.26 Installing A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Maven:

https://maven.apache.org/install.html

3.Install MySql:

https://dev.mysql.com/downloads/mysql/

4.Install MongoDb:

https://www.mongodb.com/fr-fr/cloud/atlas/efficiency

5.Install Docker for windows or mac:

https://www.docker.com/products/docker-desktop


After downloading the mysql 8 installer and installing it, you will be asked to configure the password for the default root account. This code uses the default **root** account to connect and the password can be set as **rootroot**.

Running App Post installation of MySQL, Java and Maven, you will have to set up the tables and data in the database.

For this, please run the sql commands present in file in path microservice-patient/mysql-dump/mediscreen.sql in microservice-patient.

Then install MongoDB, no username or password are required.

And then if your Operating system is not linux install docker.

Finally, you will be ready to import the code into an IDE of your choice.
Then in a terminal enter command **docker-compose up** to run Mediscreen microservices application in docker.

The code is located on the branch release.
