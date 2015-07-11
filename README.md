Customer Module Sample
===================

Technical Spec
----------------
This service is mainly implemented with [Spring Data REST](http://projects.spring.io/spring-data-rest/) and 
[Spring Security](http://projects.spring.io/spring-security/). 
And embedded [H2 Database](http://www.h2database.com/html/main.html) is adopted as the major data store of the application, 
and [Flyway](http://flywaydb.org/) is the DB initiation and migration toolkit.

On the other hand, for testing, [Spock Framework](https://github.com/spockframework/spock) is used by this project.



Build and Run
----------------
[Gradle](http://http://www.gradle.org) and Java 8 are required to build this project.

Please run `gradle clean build` under the project root (or with the assist of IDEs).
After the project being built, simply run `java -jar build/libs/rga-customer-0.1.0.jar` will launch the application.
Alternatively, run `gradle clean bootRun` can do the same thing.

After launching, the service will be available on port `8080`. And `/customer` will be the endpoint to access the `customer` resource.
Basic HTTP authentication is required for the mentioned endpoint (sample user credential `user:user`.)  
