# 📄 projectfile.md  
## Blog Site Application – IIHT Case Study

---

## ⏱ Time to Complete
**10 to 12 Hours**

---

## 📘 Application Name
**Blog Site Application**

---

## 📑 Contents

1. Problem Statement  
2. Proposed Blog Site Wireframe  
3. Application Architecture  
   - 3.1 Microservice Architecture  
4. Tool Chain  
5. Development Flow  
6. Business Requirements  
7. Proposed REST Endpoints  
8. Rubrics / Expected Deliverables  

---

## 1️⃣ Problem Statement

The **Blog Site Application** is a **RESTful Microservice-based application** that allows users to:

- Add new blogs to the system  
- Delete existing blogs from the system  
- Search blogs based on categories  
- Display all blogs created by a user  

The application is designed to support multiple blog categories and users while following modern microservice architecture principles.

The scope of this project includes developing the application using the prescribed tool chain and architectural guidelines.

---

## 2️⃣ Proposed Blog Site Wireframe

The proposed UI wireframe represents the basic interaction flow of the Blog Site Application.

Functional areas include:
- Blog site registration and blog creation  
- Searching blogs created by other users  
- Displaying blogs created by the logged-in user  
- Deleting blogs created by the logged-in user  

> **Note:**  
> UI requires improvisation and modification as per the given use case.

---

## 3️⃣ Application Architecture

The application follows a **Microservice Architecture** with clear separation of responsibilities.

### Key Components:
- Spring Boot REST APIs
- Microservices-based backend
- OpenAPI for API documentation
- Database and storage layer
- Git for source control
- JUnit for testing

---

### 3.1 Microservice Architecture  
*(Compute, Integration, OpenAPI, Networking & Content Delivery)*

Architecture highlights:
- Client SPA Web Application (JavaScript / Angular)
- API Gateway (ZUUL)
- Multiple backend microservices
- OpenAPI / Swagger documentation per microservice
- Centralized API gateway routing
- Dedicated database layer

Each microservice exposes REST APIs and communicates through the API Gateway.

---

## 4️⃣ Tool Chain

### Engineering & Development Tools

| Category | Details |
|------|--------|
| Application Language | Java |
| Compute & Integration | Spring Boot |
| Database & Storage | MySQL / SQL Server |
| Governance & Tooling | Git |
| Testing | JUnit |
| Build Tool | Maven |
| DevOps | CI/CD principles |
| Ways of Working | Engineering & Consulting Mindset |

---

## 5️⃣ Development Flow

### Evaluation Criteria
- Backend REST API development
- Database integration
- Unit testing
- Debugging and troubleshooting

### Evaluation Methods
- Code submission and evaluation
- Panel presentation

Passing score requirement: **60% in each module**

---

## 6️⃣ Business Requirements

### User Story 1: User Registration

**As a user**, I should be able to register to the Blog Site Application.

#### Registration Details:
- User Name  
- User Email ID  
- Password  

#### Acceptance Criteria:
- All fields are mandatory  
- Email ID must contain `@` and `.com`  
- Password must be alphanumeric  
- Password must be at least 8 characters  

**Only API to be developed**

---

### User Story 2: Add New Blog

**As an admin**, I should be able to add a new blog to the system.

#### Blog Fields:
- Blog Name  
- Category  
- Article  
- Author Name  
- Timestamp of creation  

#### Acceptance Criteria:
- Blog name minimum 20 characters  
- Category minimum 20 characters  
- Article minimum 1000 words  
- All fields are mandatory  
- Timestamp must be current system time  

**Only API to be developed**

---

### User Story 3: View and Delete Blogs

**As a user**, I should be able to:
- View blogs created by me  
- Delete blogs created by me  

#### Acceptance Criteria:
- User can view only their blogs  
- User can delete only their blogs  

**Only API to be developed**

---

### User Story 4: View Blog Details

**As a user**, I should be able to view blog details created by other users.

#### Acceptance Criteria:
- List blogs based on provided category  
- Display author details along with blog  
- UI provides search option by category  
- UI allows filtering by category and date range  

**API and Frontend to be developed**

---

## 7️⃣ Proposed REST Endpoints

### 7.1 REST APIs

| Method | Endpoint | Description |
|-----|--------|------------|
| POST | `/api/v1.0/blogsite/user/register` | Register new user |
| POST | `/api/v1.0/blogsite/user/blogs/add/{blogName}` | Add new blog |
| GET | `/api/v1.0/blogsite/blogs/info/{category}` | Fetch blogs by category |
| GET | `/api/v1.0/blogsite/user/getall` | Fetch blogs created by user |
| DELETE | `/api/v1.0/blogsite/user/delete/{blogName}` | Delete blog |
| GET | `/api/v1.0/blogsite/blogs/get/{category}/{fromDate}/{toDate}` | Fetch blogs by date range |

### Design Pattern Requirement
- Use **Creational Design Pattern** for composing response model objects.

---

## 8️⃣ Rubrics / Expected Deliverables

### 8.1 Engineering Concepts (Compute & Integration)

- Develop application using microservice architecture
- Use Domain Driven Design (DDD)
- Follow single data store per microservice
- Document REST APIs using OpenAPI
- Implement CQRS pattern for event sourcing
- Expose APIs through a common API Gateway

---

### 8.2 Engineering Concepts (Security & Identity)

- Restrict write operations using authentication
- Secure REST APIs using SSL certificates

---

### 8.3 Products & Frameworks (Database & Storage)

- Implement ORM using:
  - MongoRepository with MongoDB **OR**
  - JPARepository with MySQL / SQL Server
- Use custom queries with:
  - `@Query`
  - Aggregations
  - MongoTemplate (if applicable)
- Use MySQL for at least one microservice
- Implement backup when records exceed 10,000 rows

---

### 8.4 Debugging & Troubleshooting

- Generate bug reports and error logs
- Reports must include:
  - Issue description
  - Root cause
  - Suggested resolution

---

### 8.5 Unit Testing

- Unit testing using JUnit 5
- Test layers:
  - Controller
  - Service
  - Repository
- Include positive and negative test cases

---

### 8.6 Code Quality & Code Coverage

- Ensure static code analysis
- Code coverage must be **greater than 80%**

---

### 8.7 Good to Have

- Global exception handling
- Logging and monitoring
- Rate limiting
- Blog fetch time under 30 seconds
- Containerization (Docker)
- CI/CD pipeline
- Modular and maintainable codebase

---

### 8.8 Expected Outcomes

- Fully functional Blog Site Application
- REST APIs implemented as per specification
- Secure, scalable microservice solution
- Well-tested and documented system
