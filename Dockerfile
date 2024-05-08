FROM openjdk:23-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ecomm-order-processing.jar
ENTRYPOINT ["java","-jar","/ecomm-order-processing.jar"]
EXPOSE 8092