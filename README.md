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
### 2. Клонировать проект

Создать своего бота в telegram и заменить username и token в application-dev.yml и docker-compose.yml на полученные.

### 3. Запуск вручную
Убедитесь, что Redis и PostgreSQL запущены.

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```
### 4. Запуск Docker Compose
Убедитесь, что установлены Docker и Docker Compose.
В проекте уже есть файл docker-compose.yml. Запустите приложение с помощью:
```bash
docker-compose up -d
```
Чтобы остановить и удалить контейнеры, выполните:
```bash
docker-compose down
```

