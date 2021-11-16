FROM openjdk:11.0.13-slim
COPY bot.jar /bot/bot.jar
COPY application.yml /bot/application.yml
ENTRYPOINT ["java","-jar","/bot/bot.jar"]