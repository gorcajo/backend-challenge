# Description

## 1. Backend

The project has been developed with the following technologies:

- Java8 (JRE is needed in your system)
- Spring Boot
- Embedded H2 database
- Embedded Tomcat
- Maven (needed in your system if you'll compile it, alongside with the JDK)

To run you'll need the compiled JAR (it can be built using Maven with `mvn clean package`) and simply execute:

    $ java -jar backend-challenge-0.0.1-SNAPSHOT.jar

Or alternatively:

    $ mvn spring-boot:run

The server runs at port 8080 by default, but can be configured through `src/main/resources/application.properties`.

It serves all the static frontend files (see section 2).

### 1.1. Code design

Following the Spring philosofy, the application was designed using the 3-layer architecture ad MVC design pattern.

Talking about concurrency, two aspects have to be considered:

- The application has an embedded Tomcat, which manages multiple users doing requests concurrently.
- The application has an embedded H2 database, so data persistance is ensured in a concurrent way.

Both embedded Tomcat and H2 database could be removed from the application and deploy a WAR to an external Tomcat container or switch to an external DBA in any moment. The code is flexible enough to do those changes painlessly.

The code is structured as follows:

- Class `Application`: the entry point to the application.
- Package `com.guillermoorcajo.backendchallenge.pl`: Conforms the Presentation Layer (PL). Here goes all the classes related to the exposed endpoints.
- Package `com.guillermoorcajo.backendchallenge.bll`: Conforms the Business Logic Layer (BLL). Contains the application business logic, like for example the algorithms wich calculates basket amounts and its discounts.
- Package `com.guillermoorcajo.backendchallenge.dal`: Conforms the Data Access Layer (DAL). All classes dedicated to access the database are into this package.
- Package `com.guillermoorcajo.backendchallenge.dto`: Here goes all Data Transfer Objects (classes with no methods and all its fields public), which are the objects that both BLL and DAL layers move between each other to intercomunicate.
- Package `com.guillermoorcajo.backendchallenge.enums`: All enums goes here, like ProductCode. 
- Package `com.guillermoorcajo.backendchallenge.interceptors`: Package to store all classes which will intercept every request and response, for example to log the method/path of an URL and its source IP address. In the future, there can be more interceptors, for authentication for example.

Both BLL and DAL exposes interfaces to ensure that the upper layer knows which methods can be used. So, it's easy to modify the behavior of any business logic or data access details only changing an implementation class and leaving the rest of the layers as they are.

### 1.3. Database

I've decided to use an embedded H2 database, a lightweight DBA written in Java. Also, it has compatibility modes (MySQL, PostgreSQL...) to ensure SQL dialect compatibility in case of a migration to any other DBA. For this test I did not used any specific SQL dialect besides H2.

The DB is in memory mode, so every time the application is restarted, the data is wiped out and the schema is rebuilt from scratch. It would be easy to change to a file-based mode by a simple configuration line into `src/main/resources/application.properties` like:

    spring.datasource.url=jdbc:h2:/tmp/database.db:backendchallenge;DB_CLOSE_ON_EXIT=FALSE

I've created the following schema:

- `baskets`: holds the references (UUIDs) for all the baskets.
- `products`: holds all the products, with their codes, names and prices.
- `products_in_baskets`: makes a many-to-many relationship between `baskets` and `products`, allowing baskets to actually contain products, (specifying the quantities too).
- `pack_discounts`: rules for "buy 2 and get 1 free" type discounts.
- `bulk_discounts`: rules for "buy 3 or more and get a reduced price" type discounts.

Also, it has the view `view_baskets` to query baskets contents easily.

If anyone (marketing department or the CFO) wants to change prices, product names or discounts, its very easy to make a query to the database to do so (maybe with further development to expose that at an admin section into the site).

About the baskets:

- They never expire. To remove old and abandoned baskets an automated job could use `baskets.last_accessed` to decide which baskets need to be removed.
- They are public but identified by unique UUIDs; not knowing the UUID means it is impossible to access it in practice.

Notes:

- I've used JdbcTemplate instead of Hibernate to implement the data access object, but that class could be changed at any time to use another one to access the data.
- You can find more about the schema at `src/main/resources/data.sql`.
- The database console is disabled by default since this is production code.

### 1.4. Endpoints

All ndoints are in the form `/api/v1/*`, so any other endpoint design can coexist with previous versions at the same time. 

The following four endpoints were implemented:

    Method | Path                             | Request body              | OK Response
    --------------------------------------------------------------------------------------------------------
    POST   | /api/v1/basket                   |                           | HTTP 201, with the basket ID
    PUT    | /api/v1/basket/{id}              | String (the product code) | HTTP 204
    GET    | /api/v1/basket/{id}/totalamount  |                           | HTTP 200, with the basket amount
    DELETE | /api/v1/basket/{id}              |                           | HTTP 204

Examples with cURL:

    $ curl -X POST http://localhost:8080/api/v1/basket
    $ curl -X PUT --data '"MUG"' http://localhost:8080/api/v1/basket/24d44b96-999b-11e9-b15f-6fd920f6144f
    $ curl -X PUT --data '"TSHIRT"' http://localhost:8080/api/v1/basket/24d44b96-999b-11e9-b15f-6fd920f6144f
    $ curl -X GET http://localhost:8080/api/v1/basket/24d44b96-999b-11e9-b15f-6fd920f6144f
    $ curl -X DELETE http://localhost:8080/api/v1/basket/24d44b96-999b-11e9-b15f-6fd920f6144f
  
### 1.5. Logging

I've used a Java logging library called Logback. The application logs Spring messages, SQL queries, request/responses and debug messages from the business logic layer.

Each log line contains:

- Log level: DEBUG, INFO, WARN, ERROR...
- Timestamp
- Hostname (useful if this application has to be inside multiple machines or docker containers)
- Thread name
- Class and line (from the source code)
- Log message

Can be customized by editing `src/main/resources/logback.xml`. The default log level is DEBUG, depending of the environment (preproduction, production...) it could be set to INFO instead of DEBUG.
  
### 1.6. Unit tests

I considered that the only unit test this application needed was `com.guillermoorcajo.backendchallenge.unittests.BasketServiceV1Test`.

The tests can be more exhaustive, but for this code challenge I think that's enough (I didn't followed TDD). I've used JUnit for the test itself and Mckito to isolate the class under test and mock its dependencies.

## 2. Frontend

The frontend is served from the application itself, and the only page is `app.html`. If you run the app it in localhost at the port 8080 the URL would be `http://localhost:8080/app.html`.

I've used a simple combination of HTML + Bootstrap + jQuery + CSS + JS.

It is completely decoupled from the backend: only static files were used (HTML/CSS/JS) with no template engine or any other server-side manipulation; the communication with the server is done only by AJAX calls. In this way, it's very easy to substitute the frontend with a better one (the frontend team's job? :) ).

Limitations:
- I've not implemented any sort of session, login or user management. Like real ecommerce webpages, you can fill your basket without being logged in.
- It has some data hard-coded, like the products. I've focused in the backend functionalities asked (retrieve the products from the server would require another endpoint).

One possible improvement could be to use React (or another component-based frontend framework).
