# Secure Project & Task Management REST API

A backend REST API built with **Spring Boot** that enables organizations to manage projects and tasks with strict role-based access control. Different users with different roles have different permissions — enforced at both the security layer and the business logic layer.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17+ | Programming language |
| Spring Boot 4.x | Application framework |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Database ORM |
| Hibernate | JPA implementation |
| PostgreSQL | Relational database |
| Lombok | Boilerplate reduction |
| Maven | Dependency management |

---

## Project Structure

```
src/main/java/com/jt/project/
│
├── ProjectApplication.java
├── RoleInitializer.java
│
├── entity/
│   ├── Role.java
│   ├── User.java
│   ├── Project.java
│   ├── Task.java
│   ├── ProjectStatus.java
│   └── TaskStatus.java
│
├── repository/
│   ├── RoleRepository.java
│   ├── UserRepository.java
│   ├── ProjectRepository.java
│   └── TaskRepository.java
│
├── service/
│   ├── UserService.java
│   ├── ProjectService.java
│   └── TaskService.java
│
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   ├── ProjectController.java
│   └── TaskController.java
│
├── dto/
│   ├── RegisterRequestDTO.java
│   ├── UserResponseDTO.java
│   ├── ProjectRequestDTO.java
│   ├── ProjectResponseDTO.java
│   ├── TaskRequestDTO.java
│   └── TaskResponseDTO.java
│
├── security/
│   ├── AppConfig.java
│   ├── CustomUserDetailsService.java
│   └── SecurityConfig.java
│
└── exception/
    ├── ResourceNotFoundException.java
    └── BusinessRuleException.java
```

---

## Entity Relationships

```
Role (1) ──────── (Many) User
User (1) ──────── (Many) Project    [createdBy]
Project (1) ────── (Many) Task
User (1) ──────── (Many) Task       [assignedTo]
```

- `users` table holds `role_id` (FK → roles)
- `projects` table holds `created_by` (FK → users)
- `tasks` table holds `project_id` (FK → projects) and `assigned_to` (FK → users)

---

## Roles & Permissions

| Endpoint | ADMIN | MANAGER | EMPLOYEE |
|---|---|---|---|
| `POST /api/auth/register` | ✅ Public | ✅ Public | ✅ Public |
| `GET /api/users` | ✅ | ❌ | ❌ |
| `GET /api/users/{id}` | ✅ | ✅ | ✅ |
| `DELETE /api/users/{id}` | ✅ | ❌ | ❌ |
| `POST /api/projects` | ✅ | ✅ | ❌ |
| `GET /api/projects/**` | ✅ | ✅ | ✅ |
| `PUT /api/projects/{id}` | ✅ | ✅ | ❌ |
| `DELETE /api/projects/{id}` | ✅ | ✅ | ❌ |
| `POST /api/tasks` | ✅ | ✅ | ❌ |
| `GET /api/tasks/**` | ✅ | ✅ | ✅ |
| `PUT /api/tasks/{id}` | ✅ | ✅ | ✅ (own tasks only) |
| `DELETE /api/tasks/{id}` | ✅ | ✅ | ❌ |

---

## Business Rules

- A **project cannot be deleted** if it has incomplete tasks
- An **employee can only update tasks assigned to them** — not anyone else's
- A **task cannot be added** to a project with status `COMPLETED`
- A **project name must be unique**
- A **username must be unique** — duplicate registration is rejected
- Passwords are stored as **BCrypt hashes** — never plain text

---

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL installed and running
- Maven

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/secure-project-management-api.git
cd secure-project-management-api
```

### 2. Create the database

```sql
CREATE DATABASE project_management_db;
```

### 3. Configure `application.properties`

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/project_management_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.application.name=secure-project-management-api
server.port=8080
```

### 4. Run the application

```bash
mvn spring-boot:run
```

On startup, the application automatically seeds the 3 roles:
```
=== Roles initialized ===
```

---

## API Reference

### Authentication

All endpoints except `/api/auth/**` require **HTTP Basic Authentication**.

Pass credentials in every request:
```
Authorization: Basic base64(username:password)
```

In Postman: **Authorization tab → Basic Auth → enter username and password**

---

### Register a user

```
POST /api/auth/register
```

**Request body:**
```json
{
    "username": "admin",
    "password": "admin123",
    "email": "admin@company.com",
    "roleName": "ROLE_ADMIN"
}
```

Valid role names: `ROLE_ADMIN`, `ROLE_MANAGER`, `ROLE_EMPLOYEE`

**Response:**
```json
{
    "id": 1,
    "username": "admin",
    "email": "admin@company.com",
    "role": "ROLE_ADMIN"
}
```

---

### Projects

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `POST` | `/api/projects` | Create a project | ADMIN, MANAGER |
| `GET` | `/api/projects` | Get all projects | All roles |
| `GET` | `/api/projects/{id}` | Get project by id | All roles |
| `PUT` | `/api/projects/{id}` | Update a project | ADMIN, MANAGER |
| `DELETE` | `/api/projects/{id}` | Delete a project | ADMIN, MANAGER |
| `GET` | `/api/projects/status/{status}` | Get projects by status | All roles |

**Create project request:**
```json
{
    "name": "Website Redesign",
    "description": "Redesign company website",
    "createdById": 1
}
```

**Update project request:**
```json
{
    "name": "Website Redesign v2",
    "description": "Updated description",
    "status": "ON_HOLD"
}
```

Valid status values: `ACTIVE`, `COMPLETED`, `ON_HOLD`

---

### Tasks

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `POST` | `/api/tasks` | Create a task | ADMIN, MANAGER |
| `GET` | `/api/tasks` | Get all tasks | All roles |
| `GET` | `/api/tasks/{id}` | Get task by id | All roles |
| `GET` | `/api/tasks/project/{projectId}` | Get tasks by project | All roles |
| `GET` | `/api/tasks/user/{userId}` | Get tasks by assigned user | All roles |
| `PUT` | `/api/tasks/{id}` | Update a task | All roles* |
| `DELETE` | `/api/tasks/{id}` | Delete a task | ADMIN, MANAGER |

*EMPLOYEE can only update tasks assigned to them

**Create task request:**
```json
{
    "title": "Design homepage",
    "description": "Create new homepage mockup",
    "projectId": 1,
    "assignedToId": 3
}
```

**Update task request (partial update supported):**
```json
{
    "status": "IN_PROGRESS"
}
```

Valid status values: `TODO`, `IN_PROGRESS`, `DONE`

---

### Users

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `GET` | `/api/users` | Get all users | ADMIN only |
| `GET` | `/api/users/{id}` | Get user by id | All roles |
| `DELETE` | `/api/users/{id}` | Delete a user | ADMIN only |

---

## Testing with Postman

### Recommended test sequence

1. Register 3 users (ADMIN, MANAGER, EMPLOYEE) via `POST /api/auth/register`
2. Create a project as MANAGER
3. Create tasks and assign to EMPLOYEE
4. Verify EMPLOYEE can update their own task — `200 OK`
5. Verify EMPLOYEE cannot update another's task — error
6. Verify EMPLOYEE cannot create a project — `403 Forbidden`
7. Try deleting project with incomplete tasks — error
8. Mark all tasks as `DONE`, then delete project — `204 No Content`
9. Verify cascade delete — `GET /api/tasks` returns empty list
10. Verify MANAGER cannot access `GET /api/users` — `403 Forbidden`

---

## Architecture

```
Request → Security Filter Chain → Controller → Service → Repository → PostgreSQL
                ↓                      ↓           ↓
          Authentication          HTTP handling  Business    Database
          Authorization           DTO mapping    Rules       Queries
          (who are you?)          (routing)      (what's     (no logic)
          (what can you do?)                     allowed?)
```

---

## Security Implementation

- **Authentication:** HTTP Basic Auth — credentials sent with every request
- **Password storage:** BCrypt hashing via `BCryptPasswordEncoder`
- **UserDetailsService:** Custom implementation loads users from PostgreSQL
- **Session policy:** Stateless — no server-side sessions
- **CSRF:** Disabled — not needed for REST APIs
- **Role format:** Stored as `ROLE_ADMIN`, `ROLE_MANAGER`, `ROLE_EMPLOYEE`

---

## Author

**Manav** — Computer Engineering Student  
Term Project — Java Technologies (Spring Boot)

---

## License

This project is for educational purposes.
