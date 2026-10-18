# Skill Manager Web App

The Angular frontend for Skill Manager.

## Tech Stack

- Angular
- TypeScript
- SCSS
- Reactive Forms

## Features

- Browse and search IT skills.
- Filter skills by category.
- Create new skills.
- Edit existing skills.
- Delete skills.
- Use autocomplete suggestions when searching for skills.

## Requirements

- Node.js
- npm
- A running instance of the Skill Service backend.

## Development

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm start
```

The development server URL will be displayed in the terminal.

## Backend Integration

The frontend communicates with the Skill Service REST API.

Default API base URL:

```text
http://localhost:8080/api/v1/skills
```

The backend must be running for API-dependent features to work.

## License

This project is licensed under the MIT License. See the repository root [LICENSE](../../LICENSE) file.