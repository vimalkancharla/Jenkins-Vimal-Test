FROM adoptopenjdk/openjdk17

COPY target/document-management-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]