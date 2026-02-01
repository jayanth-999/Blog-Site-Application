# 🎓 Getting Started with Blog Site Application

Welcome! This guide will help you understand and run the Blog Site Application, even if you're new to Spring Boot.

---

## 📘 What is This Project?

This is a **blog platform** where users can:
- Register accounts
- Create blog posts
- Search for blogs
- Manage their own posts

It's built using **microservices** - separate, independent services that work together.

---

## 🏗️ Understanding the Architecture

### What are Microservices?

Instead of one big application, we have small, focused services:

```
Traditional App:          Microservices App:
┌──────────────┐         ┌────────┐ ┌────────┐
│              │         │ User   │ │ Blog   │
│  Everything  │    →    │Service │ │Service │
│  in One App  │         └────────┘ └────────┘
└──────────────┘              │         │
                              └────┬────┘
                              ┌────┴────┐
                              │   API   │
                              │ Gateway │
                              └─────────┘
```

**Benefits:**
- Each service can be developed independently
- Easy to scale specific parts
- Failure in one service doesn't crash everything
- Different teams can work on different services

### Our Services:

1. **User Service** (Port 8081)
   - Handles user registration
   - Validates user data

2. **Blog Service** (Port 8082)
   - Manages blog posts
   - Handles search and filtering

3. **API Gateway** (Port 8080)
   - Single entry point for all requests
   - Routes requests to the right service

---

## 🛠️ Setup Instructions

### Step 1: Install Required Software

#### Java 17
1. Download from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
2. Install and verify:
   ```bash
   java -version
   ```
   Should show: `java version "17..."`

#### Maven
1. Download from: https://maven.apache.org/download.cgi
2. Extract and add to PATH
3. Verify:
   ```bash
   mvn -version
   ```

#### MySQL
1. Download from: https://dev.mysql.com/downloads/installer/
2. Install MySQL Server
3. Remember your root password!

### Step 2: Configure MySQL

1. **Start MySQL Service**
   - Windows: Services → MySQL → Start
   - Mac: `brew services start mysql`

2. **Create Database** (Option A - Manual)
   ```sql
   mysql -u root -p
   CREATE DATABASE blogsite_db;
   exit;
   ```

   **Or (Option B - Automatic)**
   Spring Boot will create it automatically (already configured).

3. **Update Database Credentials**
   
   Edit these two files:
   - `user-service/src/main/resources/application.yml`
   - `blog-service/src/main/resources/application.yml`
   
   Change:
   ```yaml
   spring:
     datasource:
       username: root          # Your MySQL username
       password: your_password # Your MySQL password
   ```

### Step 3: Build the Project

Navigate to project root (`e:/fsc_case_study`) and run:

```bash
mvn clean install
```

This will:
- ✅ Download dependencies
- ✅ Compile code
- ✅ Run tests
- ✅ Package applications

**Takes 2-5 minutes first time!**

---

## 🚀 Running the Application

### Important: Start Services in Order

You need **3 terminal windows** open at the same time.

#### Terminal 1: User Service

```bash
cd user-service
mvn spring-boot:run
```

**Wait for:** `✅ User Service Started Successfully!`

#### Terminal 2: Blog Service

```bash
cd blog-service
mvn spring-boot:run
```

**Wait for:** `✅ Blog Service Started Successfully!`

#### Terminal 3: API Gateway

```bash
cd api-gateway
mvn spring-boot:run
```

**Wait for:** `✅ API Gateway Started Successfully!`

### Verify Services are Running

Open these URLs in your browser:

- ✅ API Gateway: http://localhost:8080/actuator/health
- ✅ User Service: http://localhost:8081/swagger-ui.html
- ✅ Blog Service: http://localhost:8082/swagger-ui.html

---

## 🧪 Testing the Application

### Option 1: Swagger UI (Easiest)

1. Open http://localhost:8081/swagger-ui.html
2. Click on "User Management"
3. Try POST `/api/v1.0/blogsite/user/register`
4. Click "Try it out"
5. Fill in sample data:
   ```json
   {
     "userName": "johndoe",
     "userEmail": "john@example.com",
     "password": "password123"
   }
   ```
6. Click "Execute"
7. See the response!

### Option 2: Postman

1. Download Postman: https://www.postman.com/downloads/
2. Create a new request:
   - Method: POST
   - URL: `http://localhost:8080/api/v1.0/blogsite/user/register`
   - Body → raw → JSON:
     ```json
     {
       "userName": "johndoe",
       "userEmail": "john@example.com",
       "password": "password123"
     }
     ```
3. Click Send

### Option 3: cURL (Command Line)

```bash
curl -X POST http://localhost:8080/api/v1.0/blogsite/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "johndoe",
    "userEmail": "john@example.com",
    "password": "password123"
  }'
```

---

## 🎯 What to Try Next

### 1. Register a User
```
POST /api/v1.0/blogsite/user/register
```

### 2. Create a Blog
```
POST /api/v1.0/blogsite/user/blogs/add/MyFirstBlog
Body:
{
  "blogName": "Understanding Spring Boot for Absolute Beginners",
  "category": "Technology and Programming Tutorials",
  "article": "[Your 1000+ character article here]",
  "authorName": "johndoe"
}
```

### 3. Search Blogs by Category
```
GET /api/v1.0/blogsite/blogs/info/Technology and Programming Tutorials
```

### 4. Get Your Blogs
```
GET /api/v1.0/blogsite/user/getall?authorName=johndoe
```

---

## 🐛 Troubleshooting

### Issue: "Communications link failure"

**Problem:** Can't connect to MySQL

**Solution:**
1. Check MySQL is running
2. Verify username/password in `application.yml`
3. Try: `mysql -u root -p` (can you login?)

### Issue: "Port 8080 already in use"

**Problem:** Something else is using the port

**Solution:**
```bash
# Find what's using the port
netstat -ano | findstr :8080    # Windows
lsof -i :8080                    # Mac/Linux

# Kill the process or change port in application.yml
```

### Issue: "Table doesn't exist"

**Problem:** Database tables not created

**Solution:**
1. Check `application.yml` has:
   ```yaml
   spring:
     jpa:
       hibernate:
         ddl-auto: update
   ```
2. Delete tables and restart services (Spring will recreate them)

### Issue: Build fails with "Failed to execute goal"

**Problem:** Test failures or compilation errors

**Solution:**
```bash
# Skip tests for now
mvn clean install -DskipTests

# Then investigate specific errors
mvn test
```

### Issue: "java.lang.OutOfMemoryError"

**Problem:** Not enough memory for Maven

**Solution:**
```bash
# Windows
set MAVEN_OPTS=-Xmx1024m

# Mac/Linux
export MAVEN_OPTS="-Xmx1024m"
```

---

## 📚 Understanding the Code

### Where to Start?

1. **User Service**:
   - `UserController.java` - REST API endpoints
   - `UserService.java` - Business logic
   - `UserRepository.java` - Database queries
   - `User.java` - Data model

2. **Look for Comments**:
   Every file has extensive comments like:
   ```java
   /**
    * ========================================
    * EXPLANATION HERE
    * ========================================
    */
   ```

3. **Read in Order**:
   - Model (Entity) → Repository → Service → Controller
   - This is the flow of data

---

## 📖 Next Steps

After you have the application running:

1. **Read**: [`SPRING_BOOT_CONCEPTS.md`](SPRING_BOOT_CONCEPTS.md) - Core concepts explained
2. **Explore**: [`API_TESTING.md`](API_TESTING.md) - More API examples
3. **Experiment**: Modify the code and see what happens!
4. **Learn**: Look at the unit tests to understand how testing works

---

## 💡 Tips for Learning

1. **Run the code first** - Don't try to understand everything before running it
2. **Use Swagger UI** - It's the easiest way to test APIs
3. **Read console output** - Lots of useful information there
4. **Start with User Service** - It's simpler than Blog Service
5. **Don't worry about understanding everything** - Focus on one concept at a time

---

## 🎓 Key Concepts You'll Learn

- ✅ RESTful APIs
- ✅ JSON request/response
- ✅ HTTP methods (GET, POST, DELETE)
- ✅ Database operations (without writing SQL!)
- ✅ Input validation
- ✅ Exception handling
- ✅ Microservices architecture
- ✅ API Gateway pattern

---

## ❓ FAQ

**Q: Do I need to know Spring before this?**
A: No! This project is designed for beginners. Read the code comments.

**Q: Can I modify the code?**
A: Absolutely! That's the best way to learn. Break things and fix them!

**Q: Why use microservices for a simple blog app?**
A: For learning! In production, you'd only use microservices for larger apps.

**Q: What if something doesn't work?**
A: Check the console logs, they usually tell you what's wrong.

**Q: Where's the frontend/UI?**
A: This project focuses on backend. You test with Swagger UI or Postman.

---

**Ready to dive deeper? Check out [SPRING_BOOT_CONCEPTS.md](SPRING_BOOT_CONCEPTS.md)!**
