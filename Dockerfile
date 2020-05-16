FROM adoptopenjdk/openjdk11:alpine-jre

ADD build/libs/socshared-vk-adapter-1.0.0-SNAPSHOT.jar /app.jar

EXPOSE 8082

ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar