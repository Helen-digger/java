# Dashboard
Dashboard informer displays weather forecast and currencies exchenge rate.

## Deployment
$ git clone https://github.com/Helen-digger/java.git --branch DashboardRel

## Getting Started
### Requared Packages

	install Java 8 https://www.digitalocean.com/community/tutorials/java-apt-get-ubuntu-16-04-ru
	gradle (version 4+) https://gradle.org/install/
	mongodb https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/

#### Prepare mongoDB

```
$ mongo
> use ***your_NameDB***
> db.createCollection("counter")
> db.counter.save({page:"all", value:"0"} )

at/project/directory/src/resources/application.properties

spring.data.mongodb.host=***HostName***
spring.data.mongodb.port=***PORTDB***
spring.data.mongodb.database=***Name_DB*** 
```

### Building and running demo
at project directory:
```
$ sh gradlew clean build
$ sh gradlew bootRun
```
#### Demo server:

http://127.0.0.1:8087

#### Change server port:

at/project/directory/src/resources/application.properties
server.port=***PORT***


### Log file
at/project/directory/logs/logs.log

## Built With
    
	1.	Vaadin Framework 8.0.6 - The web framework
	2.	Spring Boot 2.0.8 - 
	3.	mongoDB 3.4.10 - NoSQL DB
	4.	Gradle 4.2.1 - Dependency Management
	5.	log4j2.x - Logger
	6.	Apache Tomcat 8.* - Java Servlet Container 
	7.	Java 8

## Contributing

Please contact me olshevskaia94@mail.ru


## Author

* **Alena Olshevskaya** - *Initial work* - [PurpleBooth](https://github.com/Helen-digger)
