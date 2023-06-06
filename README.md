## [REST API](http://localhost:8080/doc)

## Концепция:
- Spring Modulith
  - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
  - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
  - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```
- Есть 2 общие таблицы, на которых не fk
  - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
  - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем проверять

## Аналоги
- https://java-source.net/open-source/issue-trackers

## Тестирование
- https://habr.com/ru/articles/259055/

Список выполненных задач:
1. Разобрался со структурой проекта (onboarding).
2. Удалил социальные сети: vk, yandex.
3. Вынес чувствительную информацию в отдельный проперти файл.
4. Переделал тесты так, чтоб во время тестов использовалась in memory БД (H2), а не PostgreSQL.
5. Написал тесты для всех публичных методов контроллера ProfileRestController.
6. Добавил новый функционал: добавления и удаления тегов к задаче. Фронт не делал.
7. Добавил возможность подписываться на задачи, которые не назначены на текущего пользователя. 
(Рассылку уведомлений/письма о смене статуса задачи не делал). Фронт не делал.
8. Добавил подсчет времени сколько задача находилась в работе и тестировании.
9. Написал Dockerfile для основного сервера
10. Добавил локализацию на двух языках для шаблонов писем и стартовой страницы.
