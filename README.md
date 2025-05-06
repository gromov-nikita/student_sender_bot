# Student Sender Bot

## 📌 Описание

Данный проект задумывался для автоматизации взаимодействия студентов заочной формы обучения и преподавателей.

### Цели проекта:
- Студенты могут записываться на занятия в удобные для них дни через Telegram-бота.
- Преподаватели получают список студентов, записавшихся на конкретную дату.
- Преподаватели могут отмечать присутствующих студентов.

Бот разработан с использованием Java и Spring Boot, с интеграцией Telegram Bot API.  
Для хранения состояния пользователей между запросами используется Redis, что ускоряет обработку запросов.

---

## 💡 Основной функционал

- Регистрация студентов и преподавателей
- Авторизация и разграничение ролей
- Запись студентов на занятия
- Просмотр преподавателем списка записавшихся
- Отметка присутствующих студентов

---

## 🧰 Технологии

- Java 21
- Spring Boot 3.3.3
- PostgreSQL
- Redis (Jedis)
- Telegram Bot API (`telegrambots-spring-boot-starter`)
- Gradle
- Docker
- Lombok
- Apache POI (для работы с Excel)
- StreamEx
- JUnit 5, Mockito

---

## ⚙️ Профили приложения

Проект использует два конфигурационных файла:

### `application-dev.yml` — используется для локальной разработки

### `application-docker.yml` — используется при запуске через Docker

## 🚀 Как запустить

### 1. Клонировать проект

```bash
git clone https://github.com/gromov-nikita/student_sender_bot.git
cd student_sender_bot
```
2. Запуск вручную (локально)
Убедитесь, что Redis и PostgreSQL запущены.

```bash
./gradlew bootRun --args='--spring.profiles.active=dev
```
3. Запуск через Docker
Проект уже доступен на Docker Hub. Вы можете запустить его с использованием следующей команды:

```bash
docker pull chromay/student_sender_bot
docker run --env-file .env -p 8080:8080 hromay/student_sender_bot
```
