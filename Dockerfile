FROM adoptopenjdk/openjdk11:alpine-jre

ADD build/libs/socshared-vkAdapter-1.0.0-SNAPSHOT.jar /app.jar

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar