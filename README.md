# Kotlin REST API Skeleton

A skeleton project for RESTful APIs with Kotlin and Spring Boot, following Clean Architecture principles.

## Project Description

This project serves as a starting point for developing RESTful APIs with Kotlin and Spring Boot. It implements a simple product management system with CRUD operations (Create, Read, Update, Delete) and demonstrates best practices and architectural patterns.

## Architecture

The project follows the principles of Clean Architecture (also known as Hexagonal Architecture or Ports-and-Adapters Architecture), which enables a clear separation of responsibilities and independence from frameworks and external dependencies.

### Layers

The application is divided into the following layers:

1. **API Layer** (`api`): Contains controllers, DTOs and exception handlers. This layer is responsible for communication with external clients.

2. **Application Layer** (`application`): Contains services that implement business logic. This layer orchestrates domain objects and operations.

3. **Domain Layer** (`domain`): Contains core business logic and rules, domain models and repository interfaces. This layer is independent of external frameworks and technologies.

4. **Infrastructure Layer** (`infrastructure`): Contains implementations of repository interfaces, configurations and other technical details. This layer is responsible for communication with external systems and databases.

### Benefits of this Architecture

- **Testability**: The clear separation of layers facilitates testing.
- **Maintainability**: Changes in one layer have minimal impact on other layers.
- **Flexibility**: Technologies and frameworks can be easily exchanged without affecting the core business logic.
- **Scalability**: The application can be easily extended by adding new modules.

## Project Structure

```
src
├── main
│   ├── kotlin
│   │   └── me.maximilianmilz.api.skeleton
│   │       ├── api                  # API Layer
│   │       │   ├── controller       # REST Controllers
│   │       │   ├── dto              # Data Transfer Objects
│   │       │   └── exception        # Exception Handlers
│   │       ├── application          # Application Layer
│   │       │   └── service          # Services for Business Logic
│   │       ├── domain               # Domain Layer
│   │       │   ├── model            # Domain Models
│   │       │   └── repository       # Repository Interfaces
│   │       └── infrastructure       # Infrastructure Layer
│   │           ├── config           # Configurations
│   │           └── repository       # Repository Implementations
│   └── resources
│       ├── application.properties   # Application Configuration
│       ├── static                   # Static Resources
│       └── templates                # Templates
└── test
    ├── httpClient                   # HTTP Client Tests
    └── kotlin                       # Unit and Integration Tests
```

## Technologies

- Kotlin: Modern, type-safe JVM language
- Spring Boot: Framework for developing Java/Kotlin applications
- Java 21: Current Java version
- Gradle Build tool
- Spring Web: For developing web applications and RESTful APIs
- Spring Validation: For input data validation
- Jackson: For JSON serialization and deserialization
- SpringDoc OpenAPI For API documentation
- JUnit 5: For testing

## CI/CD Pipeline

This project includes a CI/CD pipeline implemented with GitHub Actions. The pipeline automates building, testing and deploying the application.

### Pipeline Structure

The CI/CD pipeline consists of three main jobs:

1. **Build and Test**: Builds the application and runs tests
   - Sets up JDK 21
   - Builds with Gradle
   - Runs tests
   - Uploads test reports and build artifacts

2. **Build Docker Images**: Builds and pushes Docker images to Docker Hub
   - Only runs on pushes to the main branch
   - Builds and pushes both the application and database Docker images
   - Uses Docker Hub credentials stored as GitHub secrets

3. **Deploy**: Deploys the application
   - Only runs on pushes to the main branch
   - Currently configured as a placeholder
   - Can be customized based on your deployment target

### Configuration

To use the CI/CD pipeline, you need to configure the following GitHub secrets:

- `DOCKER_HUB_USERNAME`: Your Docker Hub username
- `DOCKER_HUB_TOKEN`: Your Docker Hub access token

For deployment, you may need to configure additional secrets depending on your deployment target:

- `SERVER_HOST`: The host of your deployment server
- `SERVER_USERNAME`: The username for SSH access
- `SERVER_SSH_KEY`: The SSH private key for authentication

### Running with Docker

You can also run the application locally using Docker Compose:

```bash
docker-compose up -d
```

This will start the application, PostgreSQL database and Zipkin for distributed tracing.

## Getting Started

### Prerequisites

- JDK 21 or higher
- Gradle (or use the included Gradle Wrapper)
- Docker and Docker Compose (optional, for containerized deployment)

### Starting the Application

1. Clone the repository:
   ```
   git clone https://github.com/maximilianmilz/kotlin-rest-api-skeleton.git
   cd kotlin-rest-api-skeleton
   ```

2. Start the application:
   ```
   ./gradlew bootRun
   ```

3. The application is now available at `http://localhost:8080`.

### API Documentation

The API documentation is available at `http://localhost:8080/swagger-ui.html`.

### Example Requests

In the directory `src/test/httpClient/product` you will find example requests for the Product API:

- GET `/api/v1/products`: Retrieve all products
- GET `/api/v1/products/{id}`: Retrieve a product by ID
- POST `/api/v1/products`: Create a new product
- PUT `/api/v1/products/{id}`: Update an existing product
- DELETE `/api/v1/products/{id}`: Delete a product
