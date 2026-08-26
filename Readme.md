# Task Management System

A secure and scalable Task Management System built using **Java, Spring Boot, Spring Security, JWT, MySQL, and Gradle**.

The application allows users to register and authenticate securely, create and manage tasks, assign tasks to team members, collaborate through comments and attachments, manage teams and members, and securely log out using JWT token revocation.

---

## 1. Project Overview

The Task Management System provides a centralized platform for users and teams to manage tasks and collaborate efficiently.

### Key Capabilities

- User registration
- Secure user login
- JWT-based authentication
- Secure logout using JWT token revocation
- User profile management
- Task creation
- Task assignment
- My Tasks
- Task status management
- Task completion
- Comments on tasks
- File attachments to tasks
- Team/project creation
- Team member management
- Role-based access control
- MySQL database persistence
- Swagger/OpenAPI API documentation

---

## 2. Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot | Backend framework |
| Spring Security | Authentication and authorization |
| JWT | Stateless authentication |
| Spring Data JPA | Database persistence |
| Hibernate | ORM |
| MySQL | Relational database |
| Gradle | Build and dependency management |
| Swagger / OpenAPI | API documentation and testing |
| BCrypt | Password hashing |
| Lombok | Reducing boilerplate code |

---

## 3. Architecture

The application follows a layered architecture.

```text
Client / Swagger UI
        |
        v
REST Controllers
        |
        v
Service Layer
        |
        v
Repository Layer
        |
        v
MySQL Database
```

### Security Flow

```text
Login
  |
  v
AuthService
  |
  v
Validate email/password
  |
  v
Generate JWT
  |
  v
Client
  |
  v
Authorization: Bearer <JWT>
  |
  v
JwtAuthenticationFilter
  |
  +--> Check revoked token
  |
  +--> Extract email
  |
  +--> Find user
  |
  +--> Validate JWT
  |
  v
SecurityContext
  |
  v
Protected API
```

---

## 4. Project Structure

```text
src
└── main
    └── java
        └── com.taskmanagement
            │
            ├── config
            │   ├── SecurityConfig.java
            │   └── PasswordConfig.java
            │
            ├── controller
            │   ├── AuthController.java
            │   ├── UserController.java
            │   ├── TaskController.java
            │   ├── TeamController.java
            │   ├── CommentController.java
            │   └── AttachmentController.java
            │
            ├── dto
            │   ├── LoginRequest.java
            │   ├── RegisterRequest.java
            │   ├── AuthResponse.java
            │   ├── UserResponse.java
            │   └── other DTOs
            │
            ├── entity
            │   ├── User.java
            │   ├── Task.java
            │   ├── Team.java
            │   ├── Comment.java
            │   ├── Attachment.java
            │   └── RevokedToken.java
            │
            ├── repository
            │   ├── UserRepository.java
            │   ├── TaskRepository.java
            │   ├── TeamRepository.java
            │   ├── CommentRepository.java
            │   ├── AttachmentRepository.java
            │   └── RevokedTokenRepository.java
            │
            ├── security
            │   ├── JwtService.java
            │   └── JwtAuthenticationFilter.java
            │
            └── service
                ├── AuthService.java
                ├── LogoutService.java
                ├── TaskService.java
                ├── TeamService.java
                ├── CommentService.java
                └── AttachmentService.java
```

---

## 5. Authentication and Security

The application uses JWT-based stateless authentication.

### Registration

```http
POST /api/auth/register
```

Example:

```json
{
  "name": "Atharv",
  "email": "atharv@gmail.com",
  "password": "password123"
}
```

Passwords are never stored as plain text.

BCrypt is used to hash passwords before storing them in MySQL.

### Login

```http
POST /api/auth/login
```

Example:

```json
{
  "email": "atharv@gmail.com",
  "password": "password123"
}
```

Successful login returns a JWT token.

Example:

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "tokenType": "Bearer",
  "userId": 3,
  "name": "Atharv",
  "email": "atharv@gmail.com",
  "role": "USER"
}
```

---

## 6. JWT Authentication

Protected APIs require the JWT token in the Authorization header.

```http
Authorization: Bearer <JWT_TOKEN>
```

`JwtAuthenticationFilter` performs the following operations:

1. Reads the Authorization header.
2. Extracts the JWT token.
3. Checks whether the token has been revoked.
4. Extracts the user's email from the token.
5. Finds the user from the database.
6. Validates the JWT.
7. Creates the Spring Security authentication.
8. Stores authentication in the SecurityContext.

---

## 7. Secure Logout

The application supports secure logout.

```http
POST /api/auth/logout
```

The JWT token is added to the revoked-token table.

After logout, the same token cannot be used to access protected APIs.

Example flow:

```text
Login
  |
  v
JWT Token
  |
  v
Profile API -> 200 OK
  |
  v
Logout
  |
  v
Token Revoked
  |
  v
Profile API using same token
  |
  v
401 Unauthorized
```

This provides secure logout while maintaining a stateless JWT-based authentication architecture.

---

## 8. User Management

Users can:

- Register
- Login
- View their profile
- Authenticate using JWT
- Securely logout

Example profile API:

```http
GET /api/users/profile
```

Example response:

```json
{
  "id": 3,
  "name": "Atharv",
  "email": "atharv@gmail.com",
  "role": "USER",
  "createdAt": "2026-08-13T17:29:23.720234",
  "updatedAt": "2026-08-13T17:29:23.720234"
}
```

---

## 9. Task Management

The application provides APIs for managing tasks.

Users can:

- Create tasks
- Assign tasks
- View their assigned tasks
- Update task status
- Complete tasks
- Track task information

A task contains information such as:

```text
Task ID
Title
Description
Due Date
Status
Created By
Assigned To
Created At
Updated At
```

Example task statuses:

```text
OPEN
IN_PROGRESS
COMPLETED
```

---

## 10. My Tasks

Users can retrieve tasks assigned to them.

```http
GET /api/tasks/my
```

The API identifies the authenticated user from the JWT and returns tasks assigned to that user.

---

## 11. Task Assignment

A task can be assigned to another registered user.

Example:

```json
{
  "title": "Implement login API",
  "description": "Implement JWT authentication",
  "dueDate": "2026-08-20",
  "assignedUserId": 3
}
```

The assigned user can then see the task through the My Tasks API.

---

## 12. Task Completion

Users can update the task status and complete assigned tasks.

Example flow:

```text
OPEN
  |
  v
IN_PROGRESS
  |
  v
COMPLETED
```

---

## 13. Comments

Users can collaborate on tasks through comments.

Example:

```http
POST /api/tasks/{taskId}/comments
```

Comments allow users to discuss task progress and provide additional information.

---

## 14. Attachments

Tasks support file attachments.

Users can upload and associate files with tasks.

This allows task-related documents and supporting files to be stored against the task.

---

## 15. Teams and Team Members

Users can create teams/projects and manage team membership.

Supported functionality includes:

- Create team/project
- Add team members
- View team members
- Manage team membership

Example conceptual flow:

```text
Create Team
    |
    v
Team Created
    |
    v
Add Members
    |
    v
Team Members
    |
    v
Assign Tasks
```

---

## 16. Database

The application uses MySQL.

Main entities include:

```text
users
tasks
teams
comments
attachments
revoked_tokens
```

The database is accessed using:

```text
Spring Data JPA
        +
Hibernate
        +
MySQL
```

---

## 17. Database Configuration

Configure the database in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC

spring.datasource.username=root

spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
```

Replace `YOUR_PASSWORD` with the local MySQL password.

Do not commit real database credentials to a public repository.

---

## 18. Prerequisites

Install the following:

- Java 17
- MySQL 8+
- Git
- IntelliJ IDEA or another Java IDE

Verify Java:

```powershell
java -version
```

Verify the Gradle wrapper:

```powershell
.\gradlew --version
```

---

## 19. Database Setup

Create the database:

```sql
CREATE DATABASE task_management;
```

Verify:

```sql
SHOW DATABASES;
```

Select the database:

```sql
USE task_management;
```

The application creates/updates the required tables through Hibernate based on the entity configuration.

---

## 20. Running the Application

Clone the repository:

```bash
git clone <repository-url>
```

Navigate to the project:

```powershell
cd TaskManagement
```

Build the project:

```powershell
.\gradlew clean build
```

Run the application:

```powershell
.\gradlew bootRun
```

The application runs on:

```text
http://localhost:8080
```

---

## 21. Swagger API Documentation

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Use Swagger to:

- Register users
- Login
- Authorize using JWT
- Create tasks
- View My Tasks
- Update tasks
- Add comments
- Upload attachments
- Manage teams
- Logout

### Authorizing Swagger

For protected APIs:

1. Login.
2. Copy the JWT token.
3. Click **Authorize**.
4. Enter:

```text
Bearer <JWT_TOKEN>
```

5. Execute protected APIs.

---

## 22. API Overview

| API | Method | Purpose |
|---|---|---|
| `/api/auth/register` | POST | Register user |
| `/api/auth/login` | POST | Login |
| `/api/auth/logout` | POST | Secure logout |
| `/api/users/profile` | GET | Get current user profile |
| Task APIs | POST/GET/PUT | Create and manage tasks |
| My Tasks API | GET | Get assigned tasks |
| Comment APIs | POST/GET | Manage task comments |
| Attachment APIs | POST/GET | Manage task attachments |
| Team APIs | POST/GET | Manage teams |
| Team Member APIs | POST/GET | Manage team members |

Refer to Swagger for the complete API contract and request/response models.

---

## 23. Error Handling

The application handles common authentication and authorization failures.

Example:

```text
401 Unauthorized
```

when:

- JWT is missing
- JWT is invalid
- JWT has been revoked

Example revoked-token response:

```json
{
  "error": "Token has been revoked. Please login again."
}
```

---

## 24. Testing

Build and test the application:

```powershell
.\gradlew clean build
```

Compile only:

```powershell
.\gradlew clean compileJava
```

Run the application:

```powershell
.\gradlew bootRun
```

API testing can be performed using Swagger UI.

### Secure Logout Test

1. Register a user.
2. Login.
3. Copy the JWT token.
4. Authorize Swagger using the JWT.
5. Call Profile and verify `200 OK`.
6. Call Logout and verify `200 OK`.
7. Call Profile again using the same token.
8. Verify `401 Unauthorized`.
9. Verify the application logs show that the JWT was revoked.

---

## 25. Implemented User Stories

### Core User Stories

- [x] User registration
- [x] Secure user login
- [x] JWT authentication
- [x] User profile
- [x] Create tasks
- [x] Assign tasks
- [x] View My Tasks
- [x] Update task status
- [x] Complete tasks
- [x] Add comments
- [x] Add attachments
- [x] Create teams/projects
- [x] Add team members
- [x] View team members
- [x] Secure logout

---

## 26. Optional Extensions

### Real-Time Notifications

Status:

```text
Planned / Optional
```

Possible implementation:

```text
WebSocket
or
Server-Sent Events
```

Potential notifications:

- Task assigned to user
- Task updated
- Task status changed
- Comment added

---

### Generative AI

Status:

```text
Planned / Optional
```

Potential functionality:

```text
User provides short task input
        |
        v
Generative AI
        |
        v
Task description
        |
        v
Task summary
```

This can be implemented using an LLM API to automatically generate task descriptions and summaries.

---

## 27. Security Considerations

The application implements:

- BCrypt password hashing
- JWT authentication
- Stateless sessions
- Authorization checks
- JWT signature validation
- JWT expiration
- Revoked JWT token tracking
- Protected APIs
- No plain-text password storage

Sensitive configuration such as:

```text
Database password
JWT secret
API keys
```

should be externalized using environment variables or secure configuration.

---

## 28. Future Enhancements

Possible improvements include:

- Real-time notifications using WebSockets
- Generative AI task description generation
- AI-based task summarization
- Email notifications
- Role-based team administration
- Task priority
- Task labels/tags
- Task search and filtering
- Pagination
- Audit logging
- Docker deployment
- CI/CD pipeline
- Cloud deployment

---

## 29. Conclusion

The Task Management System provides a secure backend platform for managing users, teams, tasks, collaboration, comments, and attachments.

The project demonstrates:

- REST API development
- Spring Boot architecture
- Spring Security
- JWT authentication
- Secure logout
- Database design and persistence
- JPA/Hibernate
- Team and task management
- API documentation using Swagger
- Layered backend architecture

The project can be extended further with real-time notifications and Generative AI capabilities.
---

## 30. Submission

This repository contains the completed Task Management System implementation.

The project includes secure JWT authentication, task management, team management, comments, attachments, and secure logout.

Optional extensions such as real-time notifications and Generative AI can be added as future enhancements.