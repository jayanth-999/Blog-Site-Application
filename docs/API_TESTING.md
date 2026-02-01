# 🧪 API Testing Guide

This guide shows you how to test all the endpoints in the Blog Site Application.

---

## 🎯 Testing Tools

### 1. Swagger UI (Recommended for Beginners)
- **User Service**: http://localhost:8081/swagger-ui.html
- **Blog Service**: http://localhost:8082/swagger-ui.html
- **Interactive** - Click buttons to test
- **No tools required** - Just a browser

### 2. Postman
- Download: https://www.postman.com/downloads/
- Great for saving test collections
- Easy to repeat tests

### 3. cURL (Command Line)
- Built into Windows/Mac/Linux
- Good for automation
- Examples provided below

---

## 📝 Complete API Test Scenarios

### Scenario 1: Register a New User

#### Using Swagger UI:
1. Go to http://localhost:8081/swagger-ui.html
2. Expand "User Management"
3. Click POST `/api/v1.0/blogsite/user/register`
4. Click "Try it out"
5. Enter:
```json
{
  "userName": "johndoe",
  "userEmail": "john@example.com",
  "password": "password123"
}
```
6. Click "Execute"

#### Using cURL:
```bash
curl -X POST http://localhost:8080/api/v1.0/blogsite/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "johndoe",
    "userEmail": "john@example.com",
    "password": "password123"
  }'
```

#### Expected Response (201 CREATED):
```json
{
  "id": 1,
  "userName": "johndoe",
  "userEmail": "john@example.com",
  "createdAt": "2024-01-24T10:30:00",
  "message": "User registered successfully!"
}
```

---

### Scenario 2: Test Validation (Invalid Email)

#### Request:
```bash
curl -X POST http://localhost:8080/api/v1.0/blogsite/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "testuser",
    "userEmail": "invalid-email",
    "password": "pass1234"
  }'
```

#### Expected Response (400 BAD REQUEST):
```json
{
  "timestamp": "2024-01-24T10:35:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "userEmail": "Email must contain @ and end with .com"
  }
}
```

---

### Scenario 3: Create a Blog Post

#### Request:
```bash
curl -X POST 'http://localhost:8080/api/v1.0/blogsite/user/blogs/add/MyFirstBlog' \
  -H "Content-Type: application/json" \
  -d '{
    "blogName": "Understanding Spring Boot Microservices Architecture",
    "category": "Technology and Software Development Best Practices",
    "article": "Spring Boot has revolutionized Java application development by providing a comprehensive framework that eliminates much of the boilerplate configuration traditionally required. In this article, we will explore the core concepts of Spring Boot and how it enables developers to build production-ready applications quickly. Microservices architecture has become increasingly popular in recent years as organizations seek to build scalable, maintainable systems. Spring Boot provides excellent support for microservices through Spring Cloud, which offers tools for service discovery, configuration management, and API gateways. The combination of Spring Boot and microservices allows teams to develop, deploy, and scale individual components independently, leading to improved agility and resilience. One of the key benefits of Spring Boot is its opinionated approach to configuration. Instead of requiring developers to make countless decisions about which libraries to use and how to wire them together, Spring Boot provides sensible defaults that work well for most applications. This convention-over-configuration philosophy significantly reduces development time and allows developers to focus on business logic rather than infrastructure concerns. Furthermore, Spring Boot excellent integration with modern development tools and practices, including containerization with Docker, continuous integration and deployment pipelines, and cloud platforms like AWS, Azure, and Google Cloud Platform.",
    "authorName": "johndoe"
  }'
```

#### Expected Response (201 CREATED):
```json
{
  "id": 1,
  "blogName": "Understanding Spring Boot Microservices Architecture",
  "category": "Technology and Software Development Best Practices",
  "article": "Spring Boot has revolutionized...",
  "authorName": "johndoe",
  "createdAt": "2024-01-24T10:40:00",
  "message": "Blog created successfully!"
}
```

---

### Scenario 4: Get All Blogs in a Category

#### Request:
```bash
curl -X GET 'http://localhost:8080/api/v1.0/blogsite/blogs/info/Technology%20and%20Software%20Development%20Best%20Practices'
```

**Note**: Spaces in URLs must be encoded as `%20`

#### Expected Response (200 OK):
```json
[
  {
    "id": 1,
    "blogName": "Understanding Spring Boot Microservices Architecture",
    "category": "Technology and Software Development Best Practices",
    "article": "Spring Boot has revolutionized...",
    "authorName": "johndoe",
    "createdAt": "2024-01-24T10:40:00"
  }
]
```

---

### Scenario 5: Get All Blogs by Author

#### Request:
```bash
curl -X GET 'http://localhost:8080/api/v1.0/blogsite/user/getall?authorName=johndoe'
```

#### Expected Response (200 OK):
```json
[
  {
    "id": 1,
    "blogName": "Understanding Spring Boot Microservices Architecture",
    "category": "Technology and Software Development Best Practices",
    "article": "...",
    "authorName": "johndoe",
    "createdAt": "2024-01-24T10:40:00"
  }
]
```

---

### Scenario 6: Get Blogs by Category and Date Range

#### Request:
```bash
curl -X GET 'http://localhost:8080/api/v1.0/blogsite/blogs/get/Technology%20and%20Software%20Development%20Best%20Practices/2024-01-01T00:00:00/2024-12-31T23:59:59'
```

**Date Format**: `yyyy-MM-ddTHH:mm:ss`

#### Expected Response (200 OK):
```json
[
  {
    "id": 1,
    "blogName": "Understanding Spring Boot Microservices Architecture",
    "category": "Technology and Software Development Best Practices",
    "article": "...",
    "authorName": "johndoe",
    "createdAt": "2024-01-24T10:40:00"
  }
]
```

---

### Scenario 7: Delete a Blog

#### Request:
```bash
curl -X DELETE 'http://localhost:8080/api/v1.0/blogsite/user/delete/Understanding%20Spring%20Boot%20Microservices%20Architecture'
```

#### Expected Response (204 NO CONTENT):
No body returned, just status code 204.

---

## 🧪 Postman Collection

### Import into Postman

Create a new collection with these requests:

#### 1. Register User
- Method: `POST`
- URL: `http://localhost:8080/api/v1.0/blogsite/user/register`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "userName": "{{username}}",
  "userEmail": "{{email}}",
  "password": "{{password}}"
}
```

**Variables**:
- `username`: johndoe
- `email`: john@example.com
- `password`: password123

#### 2. Create Blog
- Method: `POST`
- URL: `http://localhost:8080/api/v1.0/blogsite/user/blogs/add/{{blogName}}`
- Body: (see Scenario 3)

#### 3. Get Blogs by Category
- Method: `GET`
- URL: `http://localhost:8080/api/v1.0/blogsite/blogs/info/{{category}}`

---

## ✅ Testing Checklist

### User Service Tests

- [ ] Register with valid data → 201 Created
- [ ] Register with invalid email (no @) → 400 Bad Request
- [ ] Register with invalid email (no .com) → 400 Bad Request
- [ ] Register with short password (< 8 chars) → 400 Bad Request
- [ ] Register with non-alphanumeric password → 400 Bad Request
- [ ] Register with duplicate email → 409 Conflict
- [ ] Register with duplicate username → 409 Conflict
- [ ] Get user by email → 200 OK

### Blog Service Tests

- [ ] Create blog with valid data → 201 Created
- [ ] Create blog with short name (< 20 chars) → 400 Bad Request
- [ ] Create blog with short category (< 20 chars) → 400 Bad Request
- [ ] Create blog with short article (< 1000 chars) → 400 Bad Request
- [ ] Get blogs by category → 200 OK
- [ ] Get blogs by author → 200 OK
- [ ] Get blogs by category and date range → 200 OK
- [ ] Delete existing blog → 204 No Content
- [ ] Delete non-existent blog → 404 Not Found

---

## 🐛 Common Issues

### Issue: "Failed to fetch"
**Cause**: Service not running  
**Solution**: Check that all services are started

### Issue: 404 Not Found
**Cause**: Wrong URL or service not running  
**Solution**: Double-check URL and port

### Issue: 400 Bad Request
**Cause**: Invalid data  
**Solution**: Check response for validation errors

### Issue: 500 Internal Server Error
**Cause**: Server-side error  
**Solution**: Check service console logs

---

## 💡 Tips

1. **Start with Swagger UI** - Easiest for beginners
2. **Check Response Status Codes** - They tell you what happened
3. **Read Error Messages** - They usually explain the problem
4. **Use Variables in Postman** - Makes testing easier
5. **Save Your Requests** - You'll use them frequently

---

## 📚 HTTP Status Codes Reference

| Code | Meaning | When You See It |
|------|---------|-----------------|
| 200 | OK | Successful GET request |
| 201 | Created | Successfully created resource |
| 204 | No Content | Successfully deleted |
| 400 | Bad Request | Validation failed |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Resource already exists |
| 500 | Internal Server Error | Something went wrong server-side |

---

**Happy Testing! 🎉**
