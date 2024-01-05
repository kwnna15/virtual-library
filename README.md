# Virtual Library
A simple REST API for adding and querying information about books.

## Tech
* Java11
* Spring Boot
* Spring JPA
* Spring MVC
* Spring Security
* JUnit
* Maven
* Swagger

## Run
```
mvn clean install
mvn spring-boot:run
```

Access Swagger on: http://localhost:8080/swagger-ui/index.html

## Default login

#### User
username=user
password=password

#### Admin
username=admin
password=password

# TODO
* Add get endpoint to retrieve information about a loan given an id
* Add a return loan post endpoint
* Write tests
* Refactor - clean up the code

Optional:
* Fix race condition
* Fix shortcuts