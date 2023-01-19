# Example Project: Sandbox Service

This is a skeleton project for a REST application example written by Gergo Gergely.

## Build & Run

### Prerequisite
It is essential to have Docker running on your machine.

Open a terminal window and go to the project's root directory.
From here, there is two option to run the project, build and run locally or create a docker image and run it in a container

### Execution:

Execution steps:
1. Build the project
2. Build & Run the docker container(s)

Commands:
```
./gradlew clean build

docker-compose up
```

## Usage
When the project starts running, you can test its functions with curl, postman or browser requests.
- Populate the database with some data:
  ```
  curl --location --request POST 'http://localhost:8080/sandboxService/exampleData' \
    --header 'x-correlation-id: exampleCorrelationId' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "id": "id01",
    "status": "ACTIVE",
    "connectedData": ["a", "b"]
    }'
  ```
- Get all entities:
  ```
  curl --location --request GET 'http://localhost:8080/sandboxService/exampleData' \
    --header 'x-correlation-id: exampleCorrelationId'
  ```
- Get a specific entity:
  ```
  curl --location --request GET 'http://localhost:8080/sandboxService/exampleData/id01' \
    --header 'x-correlation-id: exampleCorrelationId'
  ```
- Edit the entity's property:
  ```
  curl --location --request PATCH 'http://localhost:8080/sandboxService/exampleData/id01?status=PAUSED' \
    --header 'x-correlation-id: exampleCorrelationId'
  ```
- Modify the whole entity:
  ```
  curl --location --request PUT 'http://localhost:8080/sandboxService/exampleData/id01' \
    --header 'x-correlation-id: exampleCorrelationId' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "id": "id01",
    "status": "ACTIVE",
    "connectedData": ["a", "b", "c", "d"]
    }'
  ```
- Delete the entity:
  ```
  curl --location --request DELETE 'http://localhost:8080/sandboxService/exampleData/id01' \
    --header 'x-correlation-id: exampleCorrelationId'
  ```

## Possible further improvements
- Logging
- API versioning
- Build an RQL interface, where the db entities could be accessed through well-defined request query params
- Improve PATCH metod to JSON PATCH
- Use linter
- Monitoring and metrics (prometheus, micrometer, etc.)
