# Skill Manager Web App

The Angular frontend for Skill Manager, providing a web interface to manage an IT skills catalog through the Skill Service REST API.

## Features

- Browse the available skills in the catalog.
- Add new skills and select their categories.
- Get autocomplete suggestions while entering a skill name.
- Detect skills that already exist in the catalog.
- Display skill categories and provide feedback for user actions.
- Browse the catalog on demand and delete skills when needed.
- Handle loading states and API errors.

## Tech Stack

- **Framework:** Angular
- **Language:** TypeScript
- **Styling:** SCSS
- **Forms:** Angular Forms
- **HTTP communication:** Angular services and RxJS
- **Development tools:** Node.js, npm, Angular CLI

## Requirements

Before running the frontend, make sure you have:

- Node.js and npm installed.
- The Skill Service backend running locally.
- The project repository cloned locally.

Check your Node.js and npm versions:

```bash
node --version
npm --version
```

## Getting Started

### 1. Install dependencies

From the repository root:

```bash
cd frontend/web-app
npm install
```

### 2. Start the backend

The frontend requires the Skill Service API to be available on port `8080`.

Follow the [backend README](../../backend/skill-service/README.md) to start PostgreSQL and run the backend.

### 3. Start the development server

From `frontend/web-app`:

```bash
npm start
```

The Angular development server starts on port `4200` by default. The terminal displays the local URL when the server is ready.

Open the URL shown in the terminal, usually:

```text
http://localhost:4200
```

## Backend Integration

The frontend communicates with the Skill Service through Angular's HTTP client.

The API service uses the relative base path:

```text
/api/v1/skills
```

During local development, `proxy.conf.json` forwards API requests to the backend at `http://localhost:8080`. This allows the frontend to use the backend without hardcoding its full URL in the application code.

The proxy is configured in the npm `start` script and is intended for local development.

If API-dependent features fail, check that PostgreSQL and the backend are running and that the backend is reachable on port `8080`.

## Build

To create a production build:

```bash
npm run build
```

The generated files are placed in the `dist/` directory.

## Project Structure

```text
web-app/
├── src/
│   ├── app/
│   │   ├── core/        # Core services and application logic
│   │   ├── features/    # Feature-specific components
│   │   └── shared/      # Reusable UI components
│   ├── styles.scss      # Global styles and design tokens
│   └── main.ts          # Application entry point
├── proxy.conf.json      # Local API proxy
├── package.json
└── angular.json
```

## License

This project is licensed under the MIT License. See the repository root [LICENSE](../../LICENSE) file.