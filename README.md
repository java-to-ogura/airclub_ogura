# airclub_ogura

This repository now contains a simple Todo REST API built with Spring Boot.

## Building and running locally

Compile and run the application using Maven:

```bash
mvn spring-boot:run
```

Alternatively, build the jar and run it with Java:

```bash
mvn package -DskipTests
java -jar target/todo-0.0.1-SNAPSHOT.jar
```

The service exposes HTTP endpoints on port `8080`:

- `GET /tasks` – list tasks
- `POST /tasks` – add a task `{ "text": "Buy milk" }`
- `PUT /tasks/{id}/done` – mark a task as done
- `DELETE /tasks/{id}` – delete a task

Tasks are persisted to `tasks.json` in the working directory.

## Docker usage

Build the Docker image and run the container:

```bash
docker build --no-cache -t todo-app .
docker run -p 8080:8080 todo-app
```

You can then interact with the API on `http://localhost:8080`.
