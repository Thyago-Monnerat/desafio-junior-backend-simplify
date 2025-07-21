FROM eclipse-temurin:24-jdk

COPY . /app

WORKDIR /app

RUN ./gradlew clean build

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "build/libs/desafiojunior-0.0.1-SNAPSHOT.jar"]