# ToDo List para gerenciar tarefas
Aplicação web de To Do List utilizando spring boot + postgres, documentado com OpenAPI. Feito para um desafio de backend júnior da Simplify

## Tecnologias usadas
* Spring MVC
* Spring Web
* H2
* PostgresSQL
* Sl4j
* OpenAPI (Swagger)
* Docker

## Como executar
Clonar repositório git  
Construir o projeto:  
```
$ docker build -t tagename .
```
Executar a aplicação:  
```
$ docker run -p 8080:8080 imageId
```

Após isso, a UI do openAPI ficará disponível em: localhost:8080/swagger-ui.html

---

# API Endpoints
Para fazer as requisições HTTP abaixo, acesse o endpoint /task.

---
Criar Tarefa - POST  
localhost:8080/task + body com as informaçãoes a serem alteradas
```
[
    {
        "descricao": "Desc Todo 1",
        "id": 1,
        "nome": "Todo 1",
        "prioridade": 1,
        "realizado": false
    }
]
```
Listar Tarefas - GET  
localhost:8080/task
```
[
    {
        "id": 1,
        "descricao": "Desc Todo 1",
        "nome": "Todo 1",
        "prioridade": 1,
        "realizado": false
    }
]
```
Atualizar Tarefa - PATCH  
localhost:8080/task/{id} + body com as informaçãoes a serem alteradas
```
[
    {
        "descricao": "Desc Todo 1 Up",
        "id": 1,
        "nome": "Todo 1 Up",
        "prioridade": 2,
        "realizado": false
    }
]
```
Remover Tarefa  
localhost:8080/task/{id}
```
localhost:8080/task/{id}
[ 
    {
        "descricao": "Desc Todo 1 Up",
        "id": 1,
        "nome": "Todo 1 Up",
        "prioridade": 2,
        "realizado": false
    }
]
```


