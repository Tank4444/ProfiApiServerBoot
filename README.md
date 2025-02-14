# Api для Профессионалов 2025 по направлению разработка веб приложений.

## Необходимое ПО

Для запуска программы нужно чтобы на сервере быть установлен JDK 21.

Для проверки нужно в консоли ввести java -version

## Развёртка

1. Скачать последнюю версию из релиза [скачать](https://github.com/Tank4444/ProfiApiServerBoot/releases)

2. Распаковать архив. 

3. Для запуска вызвать StartServer.bat

## Служебная информация
Сервер работает на порте 8080

http://localhost:8080

База данных пересоздаётся при каждом запуске

Доступ к базе данных 

http://localhost:8080/h2 

* Login: sa
* Password: password

Доступ к справочнику

http://localhost:8080/swagger-ui/index.html#/

### Registration

#### Request

URL: /registration

Body:

```json
{
  "first_name": "Alexey",
  "last_name": "Smirnov",
  "patronymic": "Ivanovich",
  "email": "user@prof.ru",
  "password": "pA1",
  "birth_date": "2001-02-15"
}
```

# Response

* Success

```
Status 201
Content-Type: application/json
{
  "data": {
    "code": 201,
    "message": "Пользователь создан",
    "user": {
      "email": "user@prof.ru",
      "name": "Alexey Smirnov Ivanovich"
    }
  }
}
```

* Valid error

```
Status 422
Content-Type: application/json
{
  "error": {
    "code": 422,
    "errors": {
      "first_name": [
        "field first_name must start with a capital letter"
      ],
      "last_name": [
        "field last_name can not be blank"
      ],
      "email": [
        "field email already exists"
      ],
      "birth_date": [
        "field birth_date can not be blank"
      ],
      "password": [
        "field password does not meet the requirements"
      ]
    },
    "message": "Validation error"
  }
}
```

