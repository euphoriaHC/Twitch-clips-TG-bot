### Description

REST API приложение для получение клипов с платформы Twitch.tv Для храниение данных используется PostgreSQL, для кэширования используется Redis. Пользователь работает с приложением через телеграм бота @Twitch_Clips_Today_Bot

### Technologies

* Java 19
* Spring boot 3.4.1
* Spring web
* Spring data jpa
* PostgreSQL 15.1
* Redis
* Junit 5 / Mockito
* Spring Doc OpenApi
* Telegram api
* Maven
* Lombok

### Endpoints

*** GET

* /api/game/gameName (Получить данные о игре, где gameName название игры)
* /api/broadcaster/casterName (Получить данные о стримере, где casterName никнейм стримера)

*** POST

* /api/clips/getClipsByGame (Получить клипы игры по игре)
* /api/clips/getClipsByBroadcaster (Получить клипы стримера по стримеру)
* /api/clips/getClipsByUserFollowList (Получить клипы стримеров по листу подписок пользователя (Лист содержит стримеров))

Для изучения и тестирования конечных точек можете использовать:
localhost:8080/swagger-ui/index.html
или 
PostMan

### Setup

1. Клонируем проект https://github.com/euphoriaHC/Twitch-clips-TG-bot.git
2. Установите базы данных PostgreSQL и Redis. Создайте таблицы с помощью команд из файла DBFILE.md
3. Регистрируемся на Twitch и регистриурем свое приложение, инструкция: https://dev.twitch.tv/docs/api/get-started/
4. Регистрируем бота в телегераме через @BotFather
5. Устанавливаем значения в файле application.properties
6. Все готово к запуску

Полезные ссылки:

https://dev.twitch.tv/docs/api/

https://core.telegram.org/bots/api

### Author

Alexey Latkin
