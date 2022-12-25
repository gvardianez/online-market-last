Проект Onlain Market

Цель проекта
Создание собственного пет-проекта на Java/Spring Boot, с применением актуальных принципов архитектурной разработки, использованием максимально современых технологий. 

Архитектура проекта:

Проект представляет собой набор из микросервисов и баз данных:

- Auth services
- Core service
- Cart service
- Promotion service
- Analytics service
- Messaging service
- Discovery service
- Config service
- Api gateway
- Frontend AngularJs
- PostgreSQL/H2 Bd
- Telegram Bot
- ELK
- Zipkin
- Redis

Используемые технологии:

- Spring boot
- Postgres
- Jwt
- Flyway/Liquibase
- Hibernate
- MapStruct
- PayPal
- Swagger
- Docker
- Redis
- Kafka
- Spring Cloud(Sleuth/Zipkin, Eureka, Config)
- Reactor(WebFlux)
- Feign client
- Junit
- WireMock
- Spring Validation, Data JPA
- ELK

Функционал проекта:

- Реализована регистрация пользователей и получение доступа к личному кабинету, со всей необходимой информацией, возможностью изменения личный данных
- Создание, изменение, удаление заказов, оплата онлайн, предусмотрены статусы
- Создание отзывов о приобретенных товарах
- Управление подписками на новости и акции
- Управление каталогом товаров (добавление, удаление, редактирование)
- Получение аналитики по товарам и пользователям
- Управление акциями на товары, которые автоматически учитываться при оформлении заказа и рассылаются в новостях подписанным пользователям
- Создание новостей, которые в свою очередь рассылаются всем подписчикам по Email и в телеграмм канале, с возможностью изменения частоты и времени рассылок
- Предусмотрена трассировка логов c отлеживанием через Zipkin, и их журналирование с использованием структуры ElasticSearch + Kibana + Logstash
- Аутентификация предусматривает получение пользователем двух токенов безопасности в микросервисе аутентификаций и далее авторизация с проверкой токенов на уровне Gateway с фильтрацией запросов
- Безопасность доступа осуществляется за счет использования Spring Gateway Api с соответсвующими настройками
- Инфраструктура обмена данными между микросервисами настроена с использованием Kafka, реактивным WebClient и FeignClient

Деплой:

Проект разворачивается локально

До старта проекта необходимо наличие образов ElasticSearch, Kibana, Logstash 7.17.7, загрузить которые можно командами:

$ docker pull elasticsearch:7.17.7

$ docker pull kibana:7.17.7

$ docker pull logstash:7.17.7

далее можно производить запуски используя только предоставленные docker-compose файлы, запускать в любой последовательности, находятся в корневых директориях проекта и микросервисов,
БД поднимаются также через docker-compose файлы, все начальные миграции осуществляются за счет Liquibase/FlyWay, все скрипты прилагаются.

После разворачивания докер контейнеров необходимо запустить микросервисы (SpringApplication.java соответственные файлы) начиная с Config, затем Discovery, после в любой последовательности. 
Основные конфиги хранятся в репозитории market-configs, все продублированы в основном проекте. При необходимости указать свои настройки портов, личных данных для подключения к телеграм боту(каналу) и GMail.

Ссылки:

ТЗ проекта: https://docs.google.com/document/d/1P45o5gSSg86HlPH3l6WyDaRnuW_Z0CQIPdBJMuCBjBE/edit?usp=sharing

Конфиг репозиторий https://github.com/gvardianez/market-configs

Некоторые скриншоты работы сайта:
https://ibb.co/rHsjG0P
https://ibb.co/9WFKDcp
https://ibb.co/bzCkHNw
https://ibb.co/6sfr0dQ
https://ibb.co/fS8Kkz5
https://ibb.co/sPswvZ5
https://ibb.co/Fhngjs1
https://ibb.co/4Nwsxyc
https://ibb.co/N3MQyTv
https://ibb.co/sKPSH68
https://ibb.co/604znR3
https://ibb.co/FXVrRYB
https://ibb.co/vYpNVPH
https://ibb.co/wY5qRPX
https://ibb.co/Gc28H9d
https://ibb.co/9sP20jD
https://ibb.co/fHtjKkf
https://ibb.co/BT3SnGz

