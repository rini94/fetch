FROM openjdk:8-jdk-alpine
COPY src /src
COPY lib /lib
COPY bin /bin
RUN javac -d /bin -cp /lib/*.jar /src/*.java
WORKDIR /bin
ENV CLASSPATH .:../lib/*
CMD tail -f /dev/null