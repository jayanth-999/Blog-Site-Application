# 🎓 Spring Boot Concepts Explained

A beginner-friendly guide to core Spring Boot concepts used in this project.

---

## 📚 Table of Contents

- [What is Spring Boot?](#what-is-spring-boot)
- [Key Annotations](#key-annotations)
- [Dependency Injection](#dependency-injection)
- [Layers of Application](#layers-of-application)
- [JPA and Databases](#jpa-and-databases)
- [REST APIs](#rest-apis)
- [Validation](#validation)
- [Exception Handling](#exception-handling)

---

## 🌱 What is Spring Boot?

**Spring Boot** is a framework that makes it easy to create production-ready Java applications.

### Before Spring Boot:
- Lots of XML configuration
- Manual setup of servers
- Complex dependency management
- Hours of setup before writing code

### With Spring Boot:
- Minimal configuration (convention over configuration)
- Embedded server (just run and go!)
- Auto-configuration (Spring figures out what you need)
- Start coding in minutes

### Example:
```java
@SpringBootApplication  // This one annotation does SO much!
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);  // Start the app!
    }
}
```

**That's it!** You have a running web server ready to handle requests.

---

## 🏷️ Key Annotations

Annotations are like labels that tell Spring what to do with your code.

### @SpringBootApplication
**What it does**: Marks the main class of your application.

**Actually combines 3 annotations**:
1. `@Configuration` - This class has configuration
2. `@EnableAutoConfiguration` - Auto-configure based on dependencies
3. `@ComponentScan` - Find and register components

```java
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

### @RestController
**What it does**: Marks a class as a REST API controller.

**Combines**: `@Controller` + `@ResponseBody`

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    // Handle HTTP requests here
}
```

### @Service
**What it does**: Marks a class as a service (business logic).

```java
@Service
public class UserService {
    // Business logic here
}
```

**Why**: Spring creates one instance and manages it for you.

### @Repository
**What it does**: Marks an interface as a data repository.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Database methods here
}
```

### @Entity
**What it does**: Marks a class as a database table.

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userName;
    // Each field = column in database
}
```

### @Autowired / @RequiredArgsConstructor
**What it does**: Injects dependencies automatically.

**Old way** (manual):
```java
public class UserService {
    private UserRepository repo = new UserRepository(); // Bad!
}
```

**Spring way** (dependency injection):
```java
@Service
@RequiredArgsConstructor  // Lombok creates constructor
public class UserService {
    private final UserRepository repo;  // Spring injects this!
}
```

---

## 💉 Dependency Injection

**Problem**: Classes need other classes to work.

**Bad Solution**: Create objects yourself with `new`
```java
public class MyService {
    private MyRepository repo = new MyRepository(); // Tightly coupled!
}
```

**Good Solution**: Let Spring inject dependencies
```java
@Service
public class MyService {
    private final MyRepository repo;  // Spring provides this
    
    public MyService(MyRepository repo) {  // Constructor injection
        this.repo = repo;
    }
}
```

**Benefits**:
- ✅ Loose coupling (easy to change implementations)
- ✅ Easy testing (can inject mock objects)
- ✅ Single instance (Spring manages lifecycle)
- ✅ No need to use `new` keyword

---

## 🏗️ Layers of Application

Spring Boot apps are organized in layers:

```
┌─────────────────────┐
│  Controller Layer   │ ← Handles HTTP (REST APIs)
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│   Service Layer     │ ← Business logic
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│ Repository Layer    │ ← Database access
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│     Database        │
└─────────────────────┘
```

### Controller Layer (REST API)
**Purpose**: Handle HTTP requests and responses.

```java
@RestController
public class UserController {
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest req) {
        // 1. Receive HTTP request
        // 2. Call service
        // 3. Return HTTP response
    }
}
```

### Service Layer (Business Logic)
**Purpose**: Perform business operations.

```java
@Service
public class UserService {
    
    public UserResponse registerUser(UserRequest req) {
        // 1. Validate data
        // 2. Check duplicates
        // 3. Save to database
        // 4. Return result
    }
}
```

### Repository Layer (Data Access)
**Purpose**: Talk to the database.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

**Why separate?**
- Each layer has one responsibility
- Easy to test each piece
- Can change database without affecting rest of code

---

## 🗄️ JPA and Databases

**JPA** = Java Persistence API (works with databases without SQL)

### How it Works

#### 1. Define an Entity (Table)
```java
@Entity  // This class = database table
@Table(name = "users")
public class User {
    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
}
```

#### 2. Create a Repository
```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring generates SQL automatically!
    Optional<User> findByEmail(String email);
}
```

**Spring generates this SQL**:
```sql
SELECT * FROM users WHERE email = ?
```

#### 3. Use in Service
```java
@Service
public class UserService {
    private final UserRepository repo;
    
    public User getUser(String email) {
        return repo.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException());
    }
}
```

### Built-in Methods (Free!)

```java
repo.save(user);           // INSERT or UPDATE
repo.findById(1L);         // SELECT by ID
repo.findAll();            // SELECT all
repo.deleteById(1L);       // DELETE
repo.count();              // COUNT
repo.existsById(1L);       // Check exists
```

### Custom Queries

**Method naming**:
```java
findByEmail(String email)               // WHERE email = ?
findByAgeGreaterThan(int age)          // WHERE age > ?
findByNameAndEmail(String name, email) // WHERE name = ? AND email = ?
```

**@Query annotation**:
```java
@Query("SELECT u FROM User u WHERE u.email = :email")
Optional<User> findUserByEmail(@Param("email") String email);
```

---

## 🌐 REST APIs

**REST** = Representational State Transfer

### HTTP Methods

| Method | Purpose | Example |
|--------|---------|---------|
| GET | Retrieve data | Get user details |
| POST | Create new resource | Register user |
| PUT | Update entire resource | Update user profile |
| PATCH | Update partial resource | Change email only |
| DELETE | Remove resource | Delete account |

### Endpoint Example

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // GET /api/users/1
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
    
    // POST /api/users
    @PostMapping
    public User createUser(@RequestBody UserRequest req) {
        return userService.createUser(req);
    }
    
    // DELETE /api/users/1
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
```

### Path Variables vs Request Parameters

**Path Variable** (`@PathVariable`):
```
GET /api/users/123
               ^^^
           This is path variable
```

**Request Parameter** (`@RequestParam`):
```
GET /api/users?name=John&age=25
               ^^^^^^^^^^^^^^^^^
           These are request parameters
```

**Request Body** (`@RequestBody`):
```json
POST /api/users
Body: {
  "name": "John",
  "email": "john@example.com"
}
```

---

## ✅ Validation

Validation ensures data is correct before processing.

### Common Annotations

```java
public class UserRequest {
    
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be 3-50 characters")
    private String name;
    
    @Email(message = "Email must be valid")
    @Pattern(regexp = ".*@.*\\.com$", message = "Email must end with .com")
    private String email;
    
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be less than 100")
    private int age;
}
```

### Using Validation

```java
@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody UserRequest req) {
    // @Valid triggers validation
    // If validation fails, Spring returns 400 Bad Request automatically
}
```

### What Happens

1. Client sends invalid data
2. Spring validates using annotations
3. If invalid, throws `MethodArgumentNotValidException`
4. Global exception handler catches it
5. Returns error response with details

---

## ❌ Exception Handling

Handle errors gracefully with meaningful messages.

### Global Exception Handler

```java
@RestControllerAdvice  // Handles exceptions globally
public class GlobalExceptionHandler {
    
    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        
        // Extract error messages
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }
    
    // Handle custom exceptions
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex) {
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

### Custom Exceptions

```java
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
```

**Usage**:
```java
User user = repo.findById(id)
    .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
```

---

## 🎯 Putting It All Together

### Example Flow: Register User

```java
// 1. CLIENT sends HTTP request
POST /api/users/register
Body: {
  "name": "John",
  "email": "john@example.com",
  "password": "pass123"
}

// 2. CONTROLLER receives request
@PostMapping("/register")
public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest req) {
    UserResponse response = userService.registerUser(req);
    return ResponseEntity.status(201).body(response);
}

// 3. SERVICE handles business logic
public UserResponse registerUser(UserRequest req) {
    // Check if user exists
    if (repo.existsByEmail(req.getEmail())) {
        throw new UserAlreadyExistsException("Email taken");
    }
    
    // Create user
    User user = User.builder()
        .name(req.getName())
        .email(req.getEmail())
        .password(req.getPassword())
        .build();
    
    // Save to database
    User saved = repo.save(user);
    
    // Return response
    return UserResponse.fromEntity(saved);
}

// 4. REPOSITORY saves to database
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}

// 5. DATABASE stores the data
INSERT INTO users (name, email, password) VALUES (?, ?, ?)

// 6. RESPONSE sent back to client
201 Created
{
  "id": 1,
  "name": "John",
  "email": "john@example.com",
  "message": "User registered successfully"
}
```

---

## 💡 Key Takeaways

1. **Spring Boot = Less configuration, more coding**
2. **Annotations tell Spring what to do** (`@Service`, `@RestController`, etc.)
3. **Dependency Injection = Let Spring manage objects**
4. **Layers = Separation of concerns** (Controller → Service → Repository)
5. **JPA = Database without SQL** (for simple operations)
6. **REST = Standard way to build APIs** (GET, POST, DELETE, etc.)
7. **Validation = Ensure data quality** (`@Valid`, `@NotBlank`, etc.)
8. **Exception Handling = Graceful error responses** (`@RestControllerAdvice`)

---

## 📖 Further Reading

- [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-boot)
- [Spring Data JPA Guide](https://spring.io/guides/gs/accessing-data-jpa/)

---

**Keep learning and building! 🚀**
