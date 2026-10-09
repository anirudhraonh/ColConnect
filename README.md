# ColConnect

A Spring Boot-based intra-college social media platform that enables seamless communication and collaboration among students and faculty members.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Authentication & Authorization**: Secure login/logout with Spring Security
- **Social Posts**: Create, view, edit, and delete posts within the college community
- **Comments & Interactions**: Comment on posts to engage with other users
- **Real-time Chat**: WebSocket-based real-time messaging system
- **Conversations**: Manage conversations with other users
- **User Search**: Search for other users by name or profile information
- **File Uploads**: AWS S3 integration for secure file and image uploads
- **Responsive UI**: Built with Thymeleaf templating engine

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.5.6
- **Language**: Java 17
- **Build Tool**: Maven
- **Security**: Spring Security 6

### Database
- **Primary**: MySQL
- **Testing**: H2 Database

### Frontend
- **Templating**: Thymeleaf with Spring Security integration
- **Real-time Communication**: WebSocket

### Additional Technologies
- **ORM**: Spring Data JPA with Hibernate
- **Cloud Storage**: AWS S3 SDK
- **Validation**: Spring Validation
- **Utility**: Lombok for reducing boilerplate code

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (for production)
- AWS Account (for S3 file uploads)

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/anirudhraonh/ColConnect.git
   cd ColConnect
   ```

2. **Navigate to the project directory**
   ```bash
   cd ColConnect
   ```

3. **Install dependencies**
   ```bash
   mvn clean install
   ```

## Configuration

### Database Configuration
Update `application.properties` or `application.yml` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/colconnect
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### AWS S3 Configuration
Configure your AWS S3 credentials for file uploads:

```properties
aws.s3.bucket-name=your-bucket-name
aws.s3.region=your-region
aws.access-key=your-access-key
aws.secret-key=your-secret-key
```

### Application Properties
- **Server Port**: Default is 8080
- **Thymeleaf Cache**: Disabled in development for faster reload

## Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using Java
```bash
mvn clean package
java -jar target/ColConnect-0.0.1-SNAPSHOT.jar
```

The application will start at `http://localhost:8080`

## API Endpoints

### Social Posts
- `GET /socials` - View all posts
- `POST /socials/addpost` - Create a new post
- `GET /socials/edit/{postId}` - Edit post form
- `POST /socials/updatepost` - Update a post
- `POST /socials/deletepost/{postId}` - Delete a post

### Chat & Messaging
- `WebSocket /ws/chat` - Real-time chat connection
- `GET /chat/conversation/{userId}` - Get or create conversation
- `GET /chat/messages/{conversationId}` - Fetch chat messages

### User & Search
- `GET /search` - Search for users
- `GET /user/profile/{userId}` - View user profile

## Project Structure

```
ColConnect/
├── src/
│   ├── main/
│   │   ├── java/com/example/ColConnect/
│   │   │   ├── ColConnectApplication.java          # Main application entry point
│   │   │   ├── configurations/                      # Spring configurations
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── WebSocketConfig.java
│   │   │   │   ├── WebConfig.java
│   │   │   │   └── SecurityBeans.java
│   │   │   ├── controllers/                         # REST controllers
│   │   │   │   ├── SocialsController.java
│   │   │   │   ├── ChatController.java
│   │   │   │   ├── SearchbarController.java
│   │   │   │   └── GlobalControllerAdvice.java
│   │   │   ├── models/                              # Entity and DTO classes
│   │   │   │   ├── User.java
│   │   │   │   ├── Post.java
│   │   │   │   ├── Comment.java
│   │   │   │   ├── ChatMessage.java
│   │   │   │   ├── Conversation.java
│   │   │   │   └── DTOs
│   │   │   ├── services/                            # Business logic
│   │   │   ├── repo/                                # Repository interfaces
│   │   │   └── utils/                               # Utility classes
│   │   └── resources/
│   │       ├── templates/                           # Thymeleaf templates
│   │       └── static/                              # CSS, JS, images
│   └── test/                                        # Test classes
├── pom.xml                                          # Maven dependencies
└── README.md
```

## Security

- **Authentication**: User credentials are securely managed by Spring Security
- **Password Hashing**: Passwords are hashed using BCrypt
- **Authorization**: Role-based access control for protected resources
- **CSRF Protection**: Enabled for form submissions
- **WebSocket Security**: Secured connections with authentication

## Development Tips

- **Hot Reload**: DevTools is enabled for faster development
- **Validation**: Input validation is enforced through Spring Validation
- **Logging**: Use SLF4J for logging throughout the application
- **Database Migrations**: Changes are handled automatically via JPA's ddl-auto

## Contributing

1. Create a new branch for your feature (`git checkout -b feature/AmazingFeature`)
2. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
3. Push to the branch (`git push origin feature/AmazingFeature`)
4. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or suggestions, please open an issue on the GitHub repository.

---

**Last Updated**: October 2026
**Version**: 0.0.1-SNAPSHOT
