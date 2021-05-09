FROM docker.mapes.info/adoptopenjdk:11

WORKDIR /app

COPY build/layers/libs /app/libs
COPY build/layers/resources /app/resources
COPY build/layers/application.jar /app/application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","/app/application.jar"]
