# Используем Gradle с JDK 21 для сборки приложения
FROM gradle:jdk21 AS build
WORKDIR /app

# Копируем только файлы конфигурации Gradle (включая Wrapper и gradle.properties)
COPY --chown=gradle:gradle gradlew gradlew.bat ./
COPY --chown=gradle:gradle gradle/ ./gradle/
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts ./
COPY --chown=gradle:gradle gradle.properties ./

# Скачиваем зависимости, чтобы они кэшировались на этом шаге
RUN ./gradlew --no-daemon dependencies

# Копируем весь исходный код после того, как зависимости уже загружены
COPY --chown=gradle:gradle . .

# Собираем проект без запуска тестов
RUN ./gradlew --no-daemon build -x test

# Финальный образ
FROM openjdk:21-jdk-slim
WORKDIR /app

# Копируем собранный jar-файл из предыдущего этапа
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]


