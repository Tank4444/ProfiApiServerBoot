# Api для Профессионалов 2025 по направлению разработка веб приложений.

## Готов

## Развёртка

Скачать последнюю версию из релиза [скачать](https://github.com/Tank4444/ProfiApiServerBoot/releases)

Распаковать архив. Запустить ProfiApiServerBoot.exe

Сервер работает на порте [ip адрес]:8080

База данных пересоздаётся при каждом запуске

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

