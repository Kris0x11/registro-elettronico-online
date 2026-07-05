# Online Electronic School Registry

Web-based school management platform developed with Spring Boot for handling academic records, attendance, disciplinary actions, and administrative operations through a role-based access system.

## Overview

Registro Elettronico Online is a full-stack web application designed to simulate a modern electronic school registry system.

The platform supports multiple user roles with dedicated functionalities:

- **Admin**
  - User and account management
  - Registry configuration
  - Access control management

- **Teachers**
  - Grade management
  - Attendance tracking
  - Disciplinary actions

- **Students**
  - Real-time access to grades
  - Attendance monitoring
  - Personal academic information

The project follows the MVC architectural pattern and focuses on backend-oriented enterprise development using Spring Boot technologies.

```
src/main/java/christian/skillfactory/registro/
│
├── configuration/
├── controller/
│   ├── admin/
│   ├── professore/
│   ├── presenza/
│   ├── sanzione/
│   ├── studente/
│   └── voto/
│
├── model/
├── repository/
├── service/
└── seed/
```
---

## Features

- Role-based authentication and authorization with Spring Security
- Secure password encryption
- CRUD operations for school entities
- Pagination support for optimized data loading
- Relational database management with JPA/Hibernate
- Many-to-Many and One-to-One relationship handling
- Server-side rendering with Thymeleaf
- Interactive frontend behavior using JavaScript


---

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Thymeleaf

### Frontend
- HTML5
- CSS3
- Bootstrap
- JavaScript

### Database
- MySQL
- MySQL Workbench

### Tools
- Maven
- Postman
- Git

---

## Architecture

The application follows the MVC (Model-View-Controller) pattern:

- **Controller Layer**
  - Request handling
  - Validation and routing
  - User interaction management

- **Service Layer**
  - Business logic
  - Credential generation
  

- **Repository Layer**
  - Data persistence
  - JPA query abstraction

- **Entity Layer**
  - Relational database mapping

---

## Security

Authentication and authorization are implemented using Spring Security.

Implemented security features include:

- Role-based access control
- Password hashing with `PasswordEncoder`
- Protected endpoints
- Session-based authentication
- Access restrictions based on user roles

---

## Database Relationships

The project includes advanced relational mapping using JPA/Hibernate:

- One-to-One
- One-to-Many
- Many-to-Many

Special attention was dedicated to:
- relationship cleanup
- transactional consistency
- referential integrity management


## Future Improvements

- Remove the remaining inline CSS styles to fully centralize styling into dedicated external stylesheet files.
- JWT authentication
- Notification system
- Responsive UI improvements
- Deisgn Improve
---

## Author

Christian D'Amato

- LinkedIn: [Christian D'Amato](https://www.linkedin.com/)
- GitHub: [Kris0x11](https://github.com/Kris0x11)

---

## License

This project was developed for educational and portfolio purposes.
