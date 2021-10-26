FROM ubuntu 
RUN apt-get update 
COPY /target/original-my-app-1.0-SNAPSHOT.jar	
