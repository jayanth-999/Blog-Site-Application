# 🌟 Blog Site Application

A **microservices-based blog platform** built with Spring Boot, demonstrating modern software architecture and best practices.

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen) ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue) ![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [For Beginners](#for-beginners)
- [Testing](#testing)
- [Contributing](#contributing)

---

## 🎯 Project Overview

The **Blog Site Application** allows users to:
- ✅ Register user accounts
- ✅ Create and publish blog posts
- ✅ Search blogs by category
- ✅ Filter blogs by date range
- ✅ View and manage their own blogs
- ✅ Delete their blog posts

This project is built for **learning purposes** with extensive code comments and documentation to help beginners understand Spring Boot microservices.

---

## ✨ Features

### User Management
- User registration with validation
- Email and username uniqueness checks
- Password validation (alphanumeric, min 8 characters)

### Blog Management
- Create blogs with rich content (min 1000 characters)
- Categorize blogs (min 20-character categories)
- Search by category
- Date range filtering
- Author-specific blog retrieval
- Delete functionality

### Technical Features
- **Microservices Architecture** - Separate services for users and blogs
- **API Gateway** - Single entry point for all requests
- **RESTful APIs** - Standard HTTP methods and status codes
- **Input Validation** - Automatic request validation
- **Exception Handling** - Global error handling with meaningful messages
- **API Documentation** - Interactive Swagger UI
- **Design Patterns** - Builder and Factory Method patterns
- **Unit Testing** - Comprehensive test coverage
- **Code Quality** - Extensive comments for beginners

---

## 🏗️ Architecture

```
┌─────────────┐
│   Clients   │ (Browser, Mobile App, Postman, etc.)
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│   API Gateway       │ Port 8080 (Entry Point)
│  (Spring Cloud)     │
└──────┬──────┬───────┘
       │      │
       ▼      ▼
┌──────────┐ ┌──────────┐
│  User    │ │  Blog    │
│ Service  │ │ Service  │
│ (8081)   │ │ (8082)   │
└────┬─────┘ └────┬─────┘
     │            │
     ▼            ▼
┌─────────────────────┐
│   MySQL Database    │
│   (blogsite_db)     │
└─────────────────────┘
```

### Microservices:

1. **User Service** (Port 8081)
   - Handles user registration
   - Validates user data
   - Manages user accounts

2. **Blog Service** (Port 8082)
   - Manages blog posts
   - Handles CRUD operations
   - Provides search and filtering

3. **API Gateway** (Port 8080)
   - Routes requests to services
   - Handles CORS
   - Future: Authentication, rate limiting

---

## 🛠️ Technologies Used

| Category | Technology | Purpose |
|----------|-----------|---------|
| **Language** | Java 17 | Programming language |
| **Framework** | Spring Boot 3.2.1 | Application framework |
| **Gateway** | Spring Cloud Gateway | API routing |
| **Database** | MySQL 8.0 | Data persistence |
| **ORM** | Spring Data JPA / Hibernate | Database operations |
| **Validation** | Jakarta Validation | Input validation |
| **Build Tool** | Maven | Dependency management |
| **Testing** | JUnit 5, Mockito | Unit testing |
| **Documentation** | SpringDoc OpenAPI | API documentation |
| **Code Quality** | JaCoCo | Code coverage |

---

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

1. **Java 17 or later**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MySQL 8.0+**
   - Download from: https://dev.mysql.com/downloads/
   - Start MySQL service

4. **Git** (optional)
   ```bash
   git --version
   ```

### Installation

1. **Clone or Download the project**
   ```bash
   cd e:/fsc_case_study
   ```

2. **Create MySQL Database**
   ```sql
   CREATE DATABASE blogsite_db;
   ```
   
   Or let Spring Boot create it automatically (already configured).

3. **Configure Database Credentials**
   
   Update `application.yml` in both services:
   - `user-service/src/main/resources/application.yml`
   - `blog-service/src/main/resources/application.yml`
   
   ```yaml
   spring:
     datasource:
       username: YOUR_MYSQL_USERNAME
       password: YOUR_MYSQL_PASSWORD
   ```

4. **Build the Project**
   ```bash
   mvn clean install
   ```

### Running the Application

You need to start **all three services**. Open **3 separate terminals**:

#### Terminal 1: User Service
```bash
cd user-service
mvn spring-boot:run
```
✅ User Service running on: http://localhost:8081

#### Terminal 2: Blog Service
```bash
cd blog-service
mvn spring-boot:run
```
✅ Blog Service running on: http://localhost:8082

#### Terminal 3: API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```
✅ API Gateway running on: http://localhost:8080

---

## 📚 API Documentation

### Interactive API Documentation (Swagger UI)

- **User Service API**: http://localhost:8081/swagger-ui.html
- **Blog Service API**: http://localhost:8082/swagger-ui.html

### API Endpoints

#### User Service

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1.0/blogsite/user/register` | Register new user |
| GET | `/api/v1.0/blogsite/user/{email}` | Get user by email |

#### Blog Service

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1.0/blogsite/user/blogs/add/{blogName}` | Create new blog |
| GET | `/api/v1.0/blogsite/blogs/info/{category}` | Get blogs by category |
| GET | `/api/v1.0/blogsite/user/getall?authorName=X` | Get blogs by author |
| DELETE | `/api/v1.0/blogsite/user/delete/{blogName}` | Delete blog |
| GET | `/api/v1.0/blogsite/blogs/get/{category}/{from}/{to}` | Get blogs by category & date |

### Example Requests

See [`docs/API_TESTING.md`](docs/API_TESTING.md) for detailed examples with cURL and Postman.

---

## 📁 Project Structure

```
fsc_case_study/
├── pom.xml                          # Parent POM (manages all modules)
├── README.md                        # This file
│
├── user-service/                    # User microservice
│   ├── src/main/java/.../
│   │   ├── controller/              # REST API endpoints
│   │   ├── service/                 # Business logic
│   │   ├── repository/              # Database access
│   │   ├── model/                   # Entity classes
│   │   ├── dto/                     # Data Transfer Objects
│   │   └── exception/               # Exception handling
│   └── src/main/resources/
│       └── application.yml          # Configuration
│
├── blog-service/                    # Blog microservice
│   ├── src/main/java/.../          # Same structure as user-service
│   └── src/main/resources/
│
├── api-gateway/                     # API Gateway
│   ├── src/main/java/.../
│   └── src/main/resources/
│       └── application.yml          # Routing configuration
│
└── docs/                            # Documentation
    ├── GETTING_STARTED.md          # Beginner's guide
    ├── SPRING_BOOT_CONCEPTS.md     # Core concepts explained
    └── API_TESTING.md              # API testing guide
```

---

## 👶 For Beginners

### New to Spring Boot?

Start with these resources in order:

1. **[Getting Started Guide](docs/GETTING_STARTED.md)**
   - Project overview for beginners
   - How to run and test
   - Troubleshooting common issues

2. **[Spring Boot Concepts](docs/SPRING_BOOT_CONCEPTS.md)**
   - Annotations explained
   - Dependency injection
   - JPA and databases
   - REST APIs

3. **[API Testing Guide](docs/API_TESTING.md)**
   - How to test APIs
   - Postman collection
   - cURL examples
   - Sample requests/responses

### Code Comments

Every file has **extensive comments** explaining:
- What the code does
- Why it's written that way
- How it works
- Examples of usage

Look for comment blocks like:
```java
/**
 * ========================================
 * DETAILED EXPLANATION HERE
 * ========================================
 */
```

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Tests for Specific Service
```bash
cd user-service
mvn test
```

### Check Code Coverage
```bash
mvn clean verify
```

View coverage report:
- `user-service/target/site/jacoco/index.html`
- `blog-service/target/site/jacoco/index.html`

### Test Coverage Goal
✅ **80%+ code coverage** as per project requirements

---

## 🎓 Learning Outcomes

By studying this project, you'll learn:

- ✅ **Microservices Architecture** - How to build distributed systems
- ✅ **Spring Boot** - Modern Java application development
- ✅ **REST APIs** - Standard web service design
- ✅ **Spring Data JPA** - Database operations without SQL
- ✅ **Validation** - Input validation best practices
- ✅ **Exception Handling** - Global error handling
- ✅ **Design Patterns** - Builder, Factory Method, Repository
- ✅ **Unit Testing** - How to write and structure tests
- ✅ **API Documentation** - OpenAPI/Swagger
- ✅ **Maven** - Dependency management and build automation

---

## 📞 Support

If you're stuck:

1. Check the code comments inline explanation
2. Read the documentation in `/docs`
3. Test endpoints using Swagger UI
4. Check the console logs for errors

---

## 📄 License

This project is created for educational purposes.

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- Baeldung Spring Tutorials
- IIHT Case Study Requirements

---

**Happy Coding! 🚀**
