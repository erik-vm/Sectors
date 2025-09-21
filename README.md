# Sectors Management System

A Spring Boot web application for managing business sectors with user registration, authentication, and hierarchical sector selection functionality. Built with Java 21, Spring Security, JPA, and Thymeleaf.

## Features

### User Management
- **User Registration & Authentication**: Secure login/logout with Spring Security
- **Role-based Access Control**: Admin and User roles with appropriate permissions
- **Profile Management**: Update personal information and change passwords
- **Account Status Management**: Active/inactive, locked/unlocked states

### Sector Management
- **Hierarchical Structure**: 4-level sector hierarchy (Manufacturing, Service, Other)
- **Admin Controls**: Create, edit, activate/deactivate sectors
- **User Submissions**: Users can select and manage their sector preferences
- **Submission Tracking**: View and edit submission history

### Admin Features
- **User Management**: View all users, block/unblock accounts
- **Dashboard**: System statistics and user activity monitoring
- **Sector Administration**: Full CRUD operations on sector hierarchy
- **Submission Oversight**: View and monitor all user submissions

## Technology Stack

- **Backend**: Spring Boot 3.5.5
- **Security**: Spring Security with BCrypt password encoding
- **Database**: PostgreSQL with Spring Data JPA
- **Frontend**: Thymeleaf templates with Bootstrap 5
- **Build Tool**: Gradle
- **Java Version**: 21

## Getting Started

### Prerequisites
- Java 21
- PostgreSQL database
- Gradle (or use the included wrapper)

### Database Setup
1. Create PostgreSQL database named `sector_app_db`
2. Update database credentials in `application.properties` if needed
3. Schema and initial data are automatically created on startup

### Running the Application
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Test Users
The application automatically creates test users:
- **Admin**: username=`admin`, password=`admin`
- **Test User**: username=`testuser`, password=`testuser`

## Project Structure

```
src/
├── main/
│   ├── java/vm/erik/sectors/
│   │   ├── config/          # Spring configuration
│   │   ├── controller/      # MVC controllers
│   │   ├── dto/            # Data transfer objects
│   │   ├── enums/          # Enumerations
│   │   ├── exception/      # Exception handling
│   │   ├── mapper/         # Entity-DTO mapping
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic
│   │   └── validation/     # Custom validators
│   └── resources/
│       ├── templates/      # Thymeleaf templates
│       ├── static/         # CSS, JS, images
│       ├── application.properties
│       ├── data.sql       # Initial sector data
│       └── schema.sql     # Database schema
```

## Architecture

The application follows a layered architecture:
- **Controllers** → **Services** → **Handlers** (except HomeController)
- Handlers use repositories directly to avoid circular dependencies
- Service layer delegates view logic to handlers
- Repository pattern with Spring Data JPA

## Key Features

### Security
- Form-based authentication with CSRF protection
- Session management with proper logout
- Role-based authorization using `@PreAuthorize`
- Password encoding with BCrypt

### Database Schema
- Complete PostgreSQL schema with indexes and constraints
- Hierarchical sector structure with parent-child relationships
- User management with roles and personal information
- Submission tracking with many-to-many sector relationships

### Validation
- Bean validation with custom validators
- Comprehensive form validation
- Business logic validation in services

## Development

### Testing
```bash
# Run tests
./gradlew test
```

### Build
```bash
# Clean build
./gradlew clean build
```

## License

This project is for educational purposes.