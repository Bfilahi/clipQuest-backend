# ClipQuest
### Overview
ClipQuest is a full-stack video streaming web application that allows users to upload, watch, and explore videos.

This is a backend project built with Spring Boot to demonstrate RESTful API design, database interaction, authentication, and authorization.

#

### Main features
- User registration & login (JWT authentication)
- CRUD operations
- Global exception handling and validation

#

### Tech Stack
- Backend: Spring Boot 3.5.6, Spring Web, Spring Security, JWT , JPA (Hibernate)
- Database: PostgreSQL
- Restful API Documentation: Swagger UI
- Language: Java 21
- Cloud: Cloudinary

#

### ERD 
<img width="675" height="304" alt="clipquest_erd" src="https://github.com/user-attachments/assets/c9e6410a-d3a8-4fb6-80da-344265625165" />

### Getting Started
1. Clone the repository
   
   ```
   git clone https://github.com/Bfilahi/clipQuest-backend.git
   ```

2. Database Setup

   * Create a PostgreSQL database.
   * Update application.properties:
     
     ```
      spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
      spring.datasource.username=yourusername
      spring.datasource.password=yourpassword
     ```

3. Run the application

   ```
   cd clipQuest-backend
   mvn spring-boot:run
   ```

#

### Restful API Documentation
#### Swagger
Once the application is running, it will be available at: http://localhost:8080/swagger-ui/index.html

#

### Author & License
&copy; 2026 <strong> Bfilahi </strong>
This project is for interview and technical demonstration purposes only. </br>
Not intended for commercial use.
