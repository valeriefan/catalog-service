# Catalog Service

This application is part of the RentSphere system and provides the functionality for managing
the houses in the catalog.

## REST API

|    Endpoint	     | Method   | Req. body | Status | Resp. body | Description    		   	                      |
|:----------------:|:--------:|:---------:|:------:|:----------:|:-------------------------------------------|
|    `/houses`     | `GET`    |           | 200    |  House[]   | Get all the houses in the catalog.         |
|    `/houses`     | `POST`   |   House   | 201    |    House    | Add a new house to the catalog.            |
|                  |          |           | 422    |            | A house with the same code already exists. |
| `/houses/{code}` | `GET`    |           | 200    |    House    | Get the house with the given code.         |
|                  |          |           | 404    |            | No house with the given code exists.       |
| `/houses/{code}` | `PUT`    |   House    | 200    |    House    | Update the house with the given code.       |
|                  |          |           | 200    |    House    | Create a house with the given code.         |
| `/houses/{code}` | `DELETE` |           | 204    |            | Delete the house with the given code.       |

## Useful Commands

| Gradle Command	         | Description                                   |
|:---------------------------|:----------------------------------------------|
| `./gradlew bootRun`        | Run the application.                          |
| `./gradlew build`          | Build the application.                        |
| `./gradlew test`           | Run tests.                                    |
| `./gradlew bootJar`        | Package the application as a JAR.             |
| `./gradlew bootBuildImage` | Package the application as a container image. |

After building the application, you can also run it from the Java CLI:

```bash
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```
