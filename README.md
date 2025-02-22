<h1 align="center"> FinGuard Finance Application </h1>

<p><b>Finguard</b> is a backend application for finance management. This project
consist of the backend implementation for the finance management service which is
built on Java, Spring Data Jpa, Spring Boot and Microservices and uses
Postman for Front-end Validation.</p>

<details>
<summary><b>How to run Fingaurd</b></summary>
<br />
<p>Run this command to clone the repo</p>

    git clone https://github.com/Krishnann-s/FinGuard.git

    cd Fingaurd

</details>

## Technologies used

<em>Finguard is a springboot, version 3.3.4, project which requires lombok configuration
if you are using intellij idea then you need to install lombok
as a extention, if you are using eclipse or Spring Tool Suite then you have to
download [Lombok](https://projectlombok.org/download) and set the IDE path</em>

Finguard is a Java application that is developed in spring tool suite which uses
various dependencies like spring gateway, spring validation... dependencies for
each Microservices is listed below in detail, kindly read through the documentation.

Database used is Oracle Database but you can simply change this by adding your
database url, username and password in the **application.properties** file.

All Microservices has it's own port number so we use the api-gateway, which runs in port 8086,
to navigate through the microservices.

All Microservices including the api-gateway is registered in eureka-server.

## Microservices

### User-Service

User-Service consist of the user registration and login business logic.
The JWT-Token is generated during the Login stage. It runs in port 8000.

#### Dependencies used

- Spring-boot-starter-validation
- Springfox-boot-starter
- spring-boot-devtools
- lombok
- spring-boot-starter-data-jpa
- spring-cloud-starter-openfeign
- spring-cloud-starter-netflix-eureka-client
- jjwt, jjwt-api, jjwt-impl, jjwt-jackson
- Spring-boot-starter-mail
- spring-boot-starter-security

#### User-Service RestController

| Definition                                          | REST APIs                              |
| :-------------------------------------------------- | -------------------------------------- |
| Register user:                                      | `POST - /finguard/user/register`       |
| Login User:                                         | `POST - /fingaurd/user/login`          |
| Get All User(only Admin can access):                | `GET - /fingaurd/users`                |
| Get User by Id:                                     | `GET - /finguard/user/{userId}`        |
| Update User by Id:                                  | `PUT - /finguard/user/{userId}`        |
| Update User wallet by User Id:                      | `PUT - /finguard/user/wallet/{userId}` |
| Delete User by Id(Both user and Admin have access): | `DELETE - /finguard/user/{userId}`     |
