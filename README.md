# Skill Service

The backend REST API of Skill Manager, built with Java and Spring Boot. It provides endpoints for managing IT skills, backed by PostgreSQL, with database schema migrations handled by Flyway.

## Features

- Create, retrieve, update, and delete skills.
- Search skills by name and filter them by category.
- Sort and paginate search results.
- Provide autocomplete suggestions for existing skills.
- Validate incoming requests.
- Handle API errors consistently.
- Maintain the database schema through versioned Flyway migrations.

## Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot
- **Web:** Spring Web
- **Persistence:** Spring Data JPA, Hibernate
- **Database:** PostgreSQL
- **Migrations:** Flyway
- **Validation:** Jakarta Validation
- **Build tool:** Maven

## Requirements

Before running the service, make sure you have:

- Java 25 installed.
- Maven installed, or a Maven Wrapper provided by the project.
- Docker and Docker Compose.
- The project repository cloned locally.

Check your Java and Maven installations:

```bash
java -version
mvn -version
```

## Getting Started

### 1. Start PostgreSQL

From the repository root, start the database using Docker Compose:

```bash
docker compose up -d
```

The Compose configuration defines the PostgreSQL container and its persistent data volume. Database credentials and the database name are supplied through the Compose environment configuration.

Check that the container is running:

```bash
docker compose ps
```

### 2. Run the backend

From the repository root:

```bash
cd backend/skill-service
mvn spring-boot:run
```

The service runs on port `8080` by default.

### 3. Database initialization and migrations

Flyway manages the database schema through versioned migration scripts stored in the backend resources.

When the application starts, Flyway applies any pending migrations before the service uses the database. This initializes the schema on a fresh database and updates an existing schema when new migrations are added.

You normally do not need to create the tables manually. Make sure PostgreSQL is running and the database connection settings are correct before starting the service.

**Important:** avoid manually modifying tables managed by Flyway. Add a new migration for schema changes so the database remains reproducible across development environments.

### 4. Build the application

To compile the application and run the Maven test suite:

```bash
mvn clean verify
```

To package the application:

```bash
mvn clean package
```

The generated artifact is placed in the `target/` directory.

## API Overview

Base path:

```text
/api/v1/skills
```

The API exposes endpoints for:

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/api/v1/skills` | List and search skills, with filtering, sorting, and pagination |
| `GET` | `/api/v1/skills/{id}` | Retrieve a skill by ID |
| `POST` | `/api/v1/skills` | Create a skill |
| `PUT` | `/api/v1/skills/{id}` | Update a skill |
| `DELETE` | `/api/v1/skills/{id}` | Delete a skill |
| `GET` | `/api/v1/skills/suggestions` | Retrieve autocomplete suggestions |

The complete request parameters, validation rules, response structures, and example payloads will be documented in the project Wiki.

## Configuration

The service connects to PostgreSQL using the application's configuration and environment variables.

The database settings must match those used by Docker Compose. Local development uses the configuration provided by the project; override sensitive values through environment variables or an appropriate local configuration when needed.

For production deployments, do not commit real credentials or secrets to version control.

See the project Wiki for the configuration reference and recommended practices for managing environment variables and secrets securely.

## Project Structure

The backend follows a conventional Spring Boot project structure:

```text
skill-service/
├── src/
│   ├── main/
│   │   ├── java/             # Application source code
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── db/migration/  # Flyway migration scripts
│   └── test/                  # Automated tests
└── pom.xml                    # Maven configuration
```

## License

This project is licensed under the MIT License. See the repository root [LICENSE](../../LICENSE) file.