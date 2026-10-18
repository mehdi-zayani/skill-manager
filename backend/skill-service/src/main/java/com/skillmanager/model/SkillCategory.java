
package com.skillmanager.model;

/**
 * Defines the categories used to classify IT skills.
 *
 * <p>Each skill is assigned one category to support consistent
 * classification, filtering, and presentation throughout the application.</p>
 *
 * <p>Categories are represented as an enum to restrict values to a
 * predefined set and prevent inconsistent category names.</p>
 */
public enum SkillCategory {

    /**
     * Programming and scripting languages, such as Java, Python,
     * JavaScript, TypeScript, SQL, and Bash.
     */
    LANGUAGE,

    /**
     * Frameworks used to build applications, such as Spring Boot,
     * Angular, Django, and Laravel.
     */
    FRAMEWORK,

    /**
     * Reusable libraries and packages, such as JUnit, Mockito,
     * Lombok, and RxJS.
     */
    LIBRARY,

    /**
     * Database management systems and data storage technologies,
     * such as PostgreSQL, MySQL, MongoDB, and Redis.
     */
    DATABASE,

    /**
     * Development and collaboration tools, such as Git, Maven,
     * Gradle, IntelliJ IDEA, and Postman.
     */
    TOOL,

    /**
     * Cloud platforms and services, such as AWS, Microsoft Azure,
     * and Google Cloud Platform.
     */
    CLOUD,

    /**
     * Containerization, infrastructure automation, and delivery
     * technologies, such as Docker, Kubernetes, Jenkins, and Terraform.
     */
    DEVOPS,

    /**
     * Software testing frameworks and tools, such as JUnit,
     * Mockito, Selenium, and Cypress.
     */
    TESTING,

    /**
     * Security technologies and standards, such as Keycloak,
     * OAuth 2.0, and OpenID Connect.
     */
    SECURITY,

    /**
     * Operating systems and runtime environments, such as Linux,
     * Windows, and the Java Virtual Machine.
     */
    PLATFORM,

    /**
     * API technologies and communication protocols, such as REST,
     * GraphQL, gRPC, and WebSocket.
     */
    API,

    /**
     * Monitoring, logging, tracing, and observability technologies,
     * such as Prometheus, Grafana, and OpenTelemetry.
     */
    MONITORING,

    /**
     * Software architecture styles and design approaches, such as
     * microservices, event-driven architecture, and domain-driven design.
     */
    ARCHITECTURE,

    /**
     * Software development methodologies and project management
     * practices, such as Scrum, Kanban, and Agile.
     */
    METHODOLOGY,

    /**
     * Skills that do not fit any of the other available categories.
     */
    OTHER
}
