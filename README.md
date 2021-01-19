# desafio-backend-votos
# Sessão Votação API

#Tecnologias utilizadas:
 - Java 8;
 - Maven;
 - Spring Webflux;
 - Swagger;
 - Mysql:5.6;
 - Redis;
 - Lombok;
 - Junit 5;

## Pre-requisitos

### REDIS
### pull
docker pull redis:3.2.5

### run
docker run -it --name redis -p 6379:6379 redis:3.2.5

### MYSQL
### pull
mysql:5.6

### run
docker run --name mysql-challenge -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=challenge -e MYSQL_USER=root -e MYSQL_PASSWORD=password -d -p 3306:3306 mysql:5.6

### SWAGGER
http://localhost:8080/swagger-ui.html#/


Obs:  Sessão: Armazenada no REDIS.
