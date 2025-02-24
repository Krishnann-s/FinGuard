<h1 align="center"> FinGuard Finance Application </h1>

<p><b>Finguard</b> is a backend application for finance management. This project consists of the backend implementation for the finance management service, built with Java, Spring Data JPA, Spring Boot, and Microservices. Postman is used for front-end validation.</p>

<h2> Introduction </h2>
FinGuard is designed to help users manage their finances efficiently. It includes features for user registration, budget management, transaction processing, investment analytics, and more.

<h2>Prerequisites</h2>
<li>Java 11 or higher</li>
<li>Maven</li>
<li>Oracle Database (or any other database with appropriate configuration)</li>
<br />
<details>
<summary><b>How to run Fingaurd</b></summary>
<br />
<p><b>1. Run this command to clone the repo</b></p>

    git clone https://github.com/Krishnann-s/FinGuard.git
    cd Fingaurd

<p><b>2. Set Up Lombok: </b></p>
<ul>
    <li> Intellij IDEA: Install Lombok as an extension. </li>
    <li> Eclipse or Spring Tool Suite: Download <a href="https://projectlombok.org/download">Lombok</a> and set the IDE Path. </li>
</ul>
<p><b>3. Configure the Database</b></p>
<ul>
    <li>Open the <code>application.properties</code> file.</li>
    <li>Update the database URL, username, password to match your database configuration.</li>
</ul>

<p><b>4.Run the application</b></p>
<ul>
    <li>Use your IDE to run the application or execute the following command</li>
    <code>./mvnw spring-boot:run</code>
</ul>
</details>

## Technologies used

FinGuard is a Spring Boot project (version 3.3.4) that requires Lombok configuration. It uses various dependencies like Spring Gateway and Spring Validation. Dependencies for each microservice are listed in detail in the [Mircoservices](#microservices) section.

The database used is Oracle Database, but you can change this by updating the database URL, username, and password in the application.properties file.

Each microservice has its own port number. The API Gateway, running on port 8086, is used to navigate through the microservices. All microservices, including the API Gateway, are registered in the Eureka server.

## Microservices

### User-Service

User-Service consist of the user registration and login business logic.
The JWT-Token is generated during the Login stage. It runs in port 8000.

<details>
<summary><b>Dependencies Needed </b></summary>
<br />
<ul>
<li> Spring-boot-starter-validation</li>
<li> Springfox-boot-starter</li>
<li> spring-boot-devtools</li>
<li> lombok</li>
<li> spring-boot-starter-data-jpa</li>
<li> spring-cloud-starter-netflix-eureka-client</li>
<li> spring-cloud-starter-openfeign</li>
<li> jjwt-api</li>
<li> jjwt-impl</li>
<li> jjwt-jackson</li>
<li> Spring-boot-starter-mail</li>
<li> spring-boot-starter-security</li>
</ul>
</details>

#### User RestController

| Definition                                          | REST APIs                              |
| :-------------------------------------------------- | -------------------------------------- |
| Register user:                                      | `POST - /finguard/user/register`       |
| Login User:                                         | `POST - /fingaurd/user/login`          |
| Get All User(only Admin can access):                | `GET - /fingaurd/users`                |
| Get User by Id:                                     | `GET - /finguard/user/{userId}`        |
| Update User by Id:                                  | `PUT - /finguard/user/{userId}`        |
| Update User wallet by User Id:                      | `PUT - /finguard/user/wallet/{userId}` |
| Delete User by Id(Both user and Admin have access): | `DELETE - /finguard/user/{userId}`     |

### Notification Service

This microservice handles sending emails when a new user registers. Below is a detailed explanation of the components and dependencies used.

<details>
<summary><b>Dependencies Used</b></summary>
<br />
<li> spring-boot-starter-test</li>
<li> spring-boot-starter</li>
<li> dotenv-java</li>
<li> spring-boot-starter-web</li>
<li> spring-boot-devtools</li>
<li> lombok</li>
<li> spring-cloud-starter-openfeign</li>
<li> spring-boot-starter-mail</li>
<li> spring-cloud-starter-netflix-eureka-client</li>
<li> springdoc-openapi-starter-webmvc-ui</li>
</details>

#### Notification RestController

| Definition                 | REST APIs                        |
| :------------------------- | -------------------------------- |
| Send Account creation mail | `POST - /notification/sendEmail` |

### Finance Management Service

The Finance Management Service handles budget management and transaction processing. It includes functionalities for creating, updating, and deleting budgets, as well as managing transactions, debts, and portfolio investments.

<details>
<summary>Dependencies Needed</summary>
<br />
<li> spring-boot-starter</li>
<li> spring-boot-starter-validation</li>
<li> springdoc-openapi-starter-webmvc-ui</li>
<li> spring-boot-starter-test</li>
<li> spring-boot-starter-data-jpa</li>
<li> spring-boot-starter-web</li>
<li> spring-cloud-starter-openfeign</li>
<li> spring-boot-devtools</li>
<li> lombok</li>
<li> ojdbc11</li>
<li> spring-cloud-starter-netflix-eureka-client</li>
<li> springfox-swagger-ui</li>
<li> hibernate-validator</li>
<li> validation-api</li>
</details>

#### Budget RestController

| Definition                     | REST APIs                                                  |
| :----------------------------- | ---------------------------------------------------------- |
| Create a new Budget            | `POST - /api/budgets`                                      |
| Get a budget by ID             | `GET - /api/budgets/{budgetId}`                            |
| Update a Budget by ID          | `PUT - /api/budgets/{budgetId} `                           |
| Delete a Budget by ID          | `DELETE - /api/budgets/{budgetId}`                         |
| Get all budgets for a user     | `GET - /api/budgets/user/{userId}`                         |
| Get remaining amount in budget | `GET - /api/budgets/{budgetId}/remaining`                  |
| Get budget report              | `GET - /api/budgets/report/{userId}/{startDate}/{endDate}` |

#### Transation RestController

| Definition                  | REST APIs                             |
| :-------------------------- | ------------------------------------- |
| Create a new transaction    | `POST - /transactions`                |
| Get a transaction by ID     | `GET - /transactions/{id}/{userName}` |
| Get transactions by user ID | `GET - /transactions/user/{userId}`   |
| Make a transaction          | `POST - /transactions/user/txn`       |
| Portfolio transaction       | `POST - /transactions/user/portfolio` |
| Debt transaction            | `POST - /transactions/user/debt`      |

### Investment Analytics Service

The Investment Analytics Service provides functionalities for managing and analyzing investment portfolios. It includes endpoints for adding, viewing, updating, and deleting portfolios, as well as generating various financial reports.

<details>
<summary><b> Dependencies Needed </b></summary>
<li> spring-boot-starter</li>
<li> spring-boot-starter-test</li>
<li> springdoc-openapi-starter-webmvc-ui</li>
<li> spring-boot-starter-web</li>
<li> spring-boot-devtools</li>
<li> ojdbc11</li>
<li> lombok</li>
<li> spring-boot-starter-data-jpa</li>
<li> spring-cloud-starter-netflix-eureka-client</li>
<li> spring-boot-starter-validation</li>
<li> javax.mail-api</li>
<li> javax.mail</li>
<li> spring-cloud-starter-openfeign</li>
</details>

#### Portfolio RestController

| Definition                     | REST APIs                                 |
| :----------------------------- | ----------------------------------------- |
| Add a new portfolio            | `POST - /api/Portfolio`                   |
| View a portfolio by ID         | `GET - /api/Portfolio/{portfolioId}`      |
| Update a portfolio             | `PUT - /api/Portfolio/{portfolioId}`      |
| Delete a portfolio             | `DELETE - /api/Portfolio/{portfolioId}`   |
| View all portfolios for a user | `GET - /api/Portfolio/portfolio/{userId}` |

#### Report RestController

| Definition                    | REST APIs                                                        |
| :---------------------------- | ---------------------------------------------------------------- |
| Get debts report              | `GET - /api/reports/financereport/{userId}`                      |
| Get income and expense report | `GET - /api/reports/incomexpensereport/{userId}`                 |
| Get budget report             | `GET - /api/reports/budgetreport/{userId}/{startDate}/{endDate}` |
| Get portfolio report          | `GET - /api/reports/portfolioreport/{userId}`                    |

### API Gateway

The API Gateway handles role-based authentication and routes requests to the appropriate microservices. It uses JWT for authentication and supports role-based access control.

<details>
<summary><b>Dependencies Needed</b></summary>
<li> spring-boot-starter-webflux</li>
<li> spring-boot-starter-security</li>
<li> spring-cloud-starter-gateway</li>
<li> jjwt-api</li>
<li> jjwt-impl</li>
<li> jjwt-jackson</li>
<li> servlet-api</li>
<li> spring-cloud-starter-netflix-eureka-client</li>
<li> spring-boot-starter-test</li>
<li> spring-boot-devtools</li>
<li> spring-cloud-starter</li>
<li> spring-security-test</li>
</details>

## Contact

If you have any questions, suggestions, or feedback, feel free to reach out:

- [Krishnann-S](https://github.com/Krishnann-s)
- [GitHub Issues](https://github.com/Krishnann-s/FinGuard/issues)
