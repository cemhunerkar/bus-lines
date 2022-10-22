# Longest Bus Lines

### Building and Running Locally

#### Prerequisites

- JDK 17

#### Building

Go to root directory and run:

`./mvnw clean install`

#### Running

After the project is built locally, you can run it with:

`./mvnw spring-boot:run`

Then go to http://localhost:8080/ in browser.

You can also use the backend endpoints directly.

http://localhost:8080/topLines?numberOfResults=10

http://localhost:8080/renew

##### Dockerized Running

Make sure you have docker running in the background.

Build the project with: `./mvnw clean install`

Build docker image with: `docker build -t top-bus-lines .`

Run docker image with: `docker run -p 8080:8080 top-bus-lines`

#### API Documentation

When the application is running, it can be accessed here:
http://localhost:8080/swagger-ui.html

### Concepts

#### Domain Driven Design

[//]: # (todo asdasd)

#### Ports and Adapters Pattern

This pattern is used in the project, it is also known as, hexagonal architecture

https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)

### Testing Strategy

#### Testing Pyramid

https://martinfowler.com/articles/practical-test-pyramid.html

#### Speed

Speed is important. For productivity, we need quick feedback during testing. Extensive usage of `@SpringBootTest` or any
other test that will start the full context will slow down the tests significantly.
So I only used it in E2E test once.
Other integration tests are done with standalone `WebTestClient` and mocks.

Millisecond testing:

https://www.youtube.com/watch?v=sUclXYMDI94

### Possible Improvements

#### Reactive

You will notice that the system is not fully reactive. In order to do so,
all components need to be reactive. Right now the repository is implemented locally with `ConcurrentHashMap` which is
not reactive.
It can be improved by introducing Hazelcast(with some tweaks) or reactive redis.
The extension point is this class: `BusLinesConfiguration`

#### Authentication and Authorization

The application does not have any security features at the moment.
Authorization and authentication methods can be added.

#### Other transportation types

Right now only bus lines are supported, other transportation methods (metro, tram, ship) can be added.

#### UI Improvements

- Dropdown selector can be added to change the number of results (10,25,50, etc)
- `Renew Data` button can be added, which will call `/renew` endpoint in the background
- Better styling
