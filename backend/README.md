# Skill Service

The backend REST API for Skill Manager, built with Java and Spring Boot.

## Tech Stack

- Java 25
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Jakarta Validation
- Maven

## Responsibilities

- Create, retrieve, update and delete skills.
- Search and filter skills by name and category.
- Provide skill suggestions for autocomplete.
- Validate incoming requests.
- Manage database schema migrations with Flyway.
- Handle API errors consistently.

## API

Base path: `/api/v1/skills`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/skills` | List and search skills |
| GET | `/api/v1/skills/{id}` | Retrieve a skill |
| POST | `/api/v1/skills` | Create a skill |
| PUT | `/api/v1/skills/{id}` | Update a skill |
| DELETE | `/api/v1/skills/{id}` | Delete a skill |
| GET | `/api/v1/skills/suggestions` | Suggest skills for autocomplete |

## Configuration

The service connects to PostgreSQL using environment variables:

- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`

Database schema changes are managed by Flyway.

## Development

Build the application:

```bash
mvn clean package
```

Run the application:

```bash
mvn spring-boot:run
```

The API runs on port `8080` by default.

## License

This project is licensed under the MIT License. See the repository root [LICENSE](../../LICENSE) file.