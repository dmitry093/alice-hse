# Система рейтингов
Пример реализации навыка для Яндекс Алиса на Spring Framework.

## Функционал
- Показ рейтинга текущего пользователя
- Добавление пользователя (только для администратора)
- Установка пользователю рейтинга (только для администратора)
- Назначение пользвователя администратором (только для администратора)

## Запуск в docker
1. Склонировать этот репозиторий:

1. Сбилдить gradle java-проект:
    <pre>gradle build</pre>

1. Сбилдить докер-образ:
    <pre>docker build . -t alice-hse</pre>

1. Запустить полученный докер образ:
    <pre>docker run -d -p 8080:8080 alice-hse</pre>

## Маршруты
- Webhook Яндекс Алиса
<pre>[POST] /webhook</pre>
- Просмотр сессий
<pre>[GET] /admin/sessions</pre>
- Просмотр списка пользователей
<pre>[GET] /admin/users</pre>
- Добавление пользователя
<pre>[POST] /admin/user/</pre>


## Использованные технологии
- <a href="https://dialogs.yandex.ru/">Яндекс диалоги</a>
- <a href="https://spring.io/">Spring Framework</a>
- <a href="https://www.docker.com/">Docker</a>

